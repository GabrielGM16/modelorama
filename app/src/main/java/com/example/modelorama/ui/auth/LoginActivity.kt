package com.example.modelorama.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.modelorama.MainActivity
import com.example.modelorama.databinding.ActivityLoginBinding
import com.example.modelorama.utils.DefaultUserManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            // Initialize Firebase Auth
            auth = FirebaseAuth.getInstance()
            
            // Set up click listeners
            binding.buttonLogin.setOnClickListener {
                loginUser()
            }
            
            binding.textViewRegister.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            
            binding.buttonQuickLogin.setOnClickListener {
                showLoading(true)
                lifecycleScope.launch {
                    try {
                        val success = DefaultUserManager.createDefaultUserIfNeeded()
                        if (success) {
                            navigateToMain()
                        } else {
                            showError("No se pudo crear la cuenta por defecto. Verifica tu conexión a internet.")
                        }
                    } catch (e: Exception) {
                        val errorMessage = when {
                            e.message?.contains("network") == true -> "Error de red. Verifica tu conexión a internet."
                            e.message?.contains("password") == true -> "Error en la contraseña. Contacta al desarrollador."
                            e.message?.contains("email") == true -> "Error en el correo electrónico. Contacta al desarrollador."
                            e.message?.contains("already in use") == true -> {
                                // Si el email ya está en uso, intentamos iniciar sesión directamente
                                try {
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                        DefaultUserManager.getDefaultCredentials().first,
                                        DefaultUserManager.getDefaultCredentials().second
                                    ).await()
                                    navigateToMain()
                                    return@launch
                                } catch (loginError: Exception) {
                                    "El correo ya está registrado pero no se pudo iniciar sesión: ${loginError.message}"
                                }
                            }
                            else -> "Error: ${e.message}"
                        }
                        showError(errorMessage)
                    } finally {
                        showLoading(false)
                    }
                }
            }
        } catch (e: Exception) {
            // Log the error and show a toast
            Log.e("LoginActivity", "Error initializing UI: ${e.message}", e)
            Toast.makeText(this, "Error initializing app: ${e.message}", Toast.LENGTH_LONG).show()
            // Set a simple content view as fallback
            setContentView(TextView(this).apply {
                text = "Error loading app. Please restart."
                gravity = Gravity.CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            })
        }
    }

    private fun loginUser() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false) // Make sure loading is hidden
                if (task.isSuccessful) {
                    try {
                        navigateToMain()
                    } catch (e: Exception) {
                        Log.e("LoginActivity", "Error navigating to main: ${e.message}", e)
                        Toast.makeText(this, "Error al navegar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    showError("Error de inicio de sesión: ${task.exception?.message}")
                }
            }
    }

    private fun navigateToMain() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error navigating to main: ${e.message}", e)
            Toast.makeText(this, "Error al navegar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonLogin.isEnabled = !isLoading
        binding.buttonQuickLogin.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
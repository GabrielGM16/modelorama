package com.example.modelorama.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.MainActivity
import com.example.modelorama.databinding.ActivityRegisterBinding
import com.example.modelorama.utils.DefaultUserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.registerButton.setOnClickListener {
            registerUser()
        }

        // You need to add this TextView to your layout or remove this code
        // binding.textViewLogin.setOnClickListener {
        //     finish()
        // }
        
        // Add default user button functionality
        binding.buttonUseDefault.setOnClickListener {
            val credentials = DefaultUserManager.getDefaultCredentials()
            binding.nameEditText.setText("Usuario Modelorama")
            binding.emailEditText.setText(credentials.first)
            binding.passwordEditText.setText(credentials.second)
            binding.confirmPasswordEditText.setText(credentials.second)
        }
    }

    private fun registerUser() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "nombre" to name,
                        "email" to email,
                        "id" to user?.uid,
                        "fechaRegistro" to System.currentTimeMillis()
                    )

                    user?.uid?.let { uid ->
                        db.collection("usuarios").document(uid).set(userData)
                            .addOnSuccessListener {
                                showLoading(false) // Make sure to hide loading here
                                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                navigateToMain()
                            }
                            .addOnFailureListener { e ->
                                showLoading(false) // Make sure to hide loading here
                                showError("Error al guardar datos: ${e.message}")
                            }
                    } ?: run {
                        // Handle case where user is null
                        showLoading(false)
                        showError("Error: No se pudo obtener el usuario")
                    }
                } else {
                    showLoading(false) // Make sure to hide loading here
                    showError("Error de registro: ${task.exception?.message}")
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
            Log.e("RegisterActivity", "Error navigating to main: ${e.message}", e)
            Toast.makeText(this, "Error al navegar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.registerButton.isEnabled = !isLoading
        binding.buttonUseDefault.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
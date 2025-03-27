package com.example.modelorama.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        // Set up register button click listener
        binding.registerButton.setOnClickListener {
            registerUser()
        }
    }
    
    private fun registerUser() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
        
        // Add validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showToast("Please fill all fields")
            return
        }
        
        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return
        }
        
        // Show loading indicator
        binding.progressBar.visibility = View.VISIBLE
        binding.registerButton.isEnabled = false
        
        // Add detailed logging
        Log.d("RegisterActivity", "Attempting to register user: $email")
        
        // Call your authentication service
        // Keep this file as is, it's more complete with better error handling
        
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = View.GONE
                binding.registerButton.isEnabled = true
                
                if (task.isSuccessful) {
                    // Save additional user info if needed
                    saveUserInfo(name, email)
                    showToast("Registration successful")
                    navigateToLogin()
                } else {
                    // Captura y muestra el error completo
                    val exception = task.exception
                    Log.e("RegisterActivity", "Registration failed", exception)
                    
                    // Muestra el error completo en el Logcat
                    val fullErrorMessage = "Error completo: ${exception?.javaClass?.simpleName}: ${exception?.message}"
                    Log.e("RegisterActivity", fullErrorMessage)
                    
                    // Muestra un mensaje más detallado al usuario
                    val errorMessage = when {
                        exception is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil. Usa al menos 6 caracteres."
                        exception is FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
                        exception is FirebaseAuthInvalidCredentialsException -> "Formato de correo inválido."
                        exception?.message?.contains("network") == true -> "Error de red. Verifica tu conexión."
                        else -> "Error de registro: ${exception?.message ?: "Error desconocido"}"
                    }
                    
                    // Muestra el mensaje de error en un Toast más largo
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    
                    // También puedes mostrar el error en un TextView en la pantalla
                    // binding.errorTextView.text = errorMessage
                    // binding.errorTextView.visibility = View.VISIBLE
                }
            }
    }
    
    private fun saveUserInfo(name: String, email: String) {
        val userId = auth.currentUser?.uid ?: return
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )
        
        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User information saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Error saving user information", e)
            }
    }
    
    private fun navigateToLogin() {
        // Navigate to login screen or main activity
        // Replace LoginActivity::class.java with your actual login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
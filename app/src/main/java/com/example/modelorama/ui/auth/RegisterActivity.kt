package com.example.modelorama.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI elements
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        registerButton = findViewById(R.id.buttonRegister)
        progressBar = findViewById(R.id.progressBar)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Validate inputs
            if (name.isEmpty()) {
                nameEditText.error = "Name is required"
                nameEditText.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "Password should be at least 6 characters"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordEditText.error = "Passwords don't match"
                confirmPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        // Show loading indicator
        progressBar.visibility = View.VISIBLE
        
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Hide loading indicator
                progressBar.visibility = View.GONE
                
                if (task.isSuccessful) {
                    // Registration successful
                    val user = FirebaseAuth.getInstance().currentUser
                    // Save additional user data to Firestore if needed
                    saveUserDataToFirestore(user?.uid)
                    
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    // Navigate to login or main activity
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    // Registration failed
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "Password is too weak. Use at least 6 characters."
                        is FirebaseAuthUserCollisionException -> "This email is already registered."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                        else -> "Registration failed: ${task.exception?.message}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("RegisterActivity", "Error: ${task.exception}")
                }
            }
    }

    private fun saveUserDataToFirestore(userId: String?) {
        userId?.let {
            val db = FirebaseFirestore.getInstance()
            val userData = hashMapOf(
                "name" to nameEditText.text.toString(),
                "email" to emailEditText.text.toString(),
                "createdAt" to FieldValue.serverTimestamp()
            )
            
            db.collection("users").document(it)
                .set(userData)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "User data saved to Firestore")
                }
                .addOnFailureListener { e ->
                    Log.e("RegisterActivity", "Error saving user data: $e")
                }
        }
    }
}
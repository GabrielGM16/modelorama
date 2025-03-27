package com.example.modelorama.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object DefaultUserManager {
    private const val DEFAULT_EMAIL = "usuario@modelorama.com"
    private const val DEFAULT_PASSWORD = "Modelorama123"
    private const val DEFAULT_NAME = "Usuario Modelorama"
    
    suspend fun createDefaultUserIfNeeded(): Boolean {
        val auth = FirebaseAuth.getInstance()
        
        return try {
            // Try to sign in with default user
            try {
                auth.signInWithEmailAndPassword(DEFAULT_EMAIL, DEFAULT_PASSWORD).await()
                // If we get here, user already exists
                return true
            } catch (e: Exception) {
                // User doesn't exist, create it
                val result = auth.createUserWithEmailAndPassword(DEFAULT_EMAIL, DEFAULT_PASSWORD).await()
                
                // Save additional info to Firestore
                result.user?.let { user ->
                    saveUserToFirestore(user)
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }
    
    private suspend fun saveUserToFirestore(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val userData = hashMapOf(
            "nombre" to DEFAULT_NAME,
            "email" to DEFAULT_EMAIL,
            "id" to user.uid,
            "fechaRegistro" to System.currentTimeMillis()
        )
        
        db.collection("usuarios").document(user.uid).set(userData).await()
    }
    
    fun getDefaultCredentials(): Pair<String, String> {
        return Pair(DEFAULT_EMAIL, DEFAULT_PASSWORD)
    }
}
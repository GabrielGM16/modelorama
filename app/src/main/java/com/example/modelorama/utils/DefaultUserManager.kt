package com.example.modelorama.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object DefaultUserManager {
    private const val TAG = "DefaultUserManager"
    private const val DEFAULT_EMAIL = "usuario@modelorama.com"
    private const val DEFAULT_PASSWORD = "Modelorama123"
    private const val DEFAULT_NAME = "Usuario Modelorama"
    
    suspend fun createDefaultUserIfNeeded(): Boolean {
        val auth = FirebaseAuth.getInstance()
        
        return try {
            // Intenta iniciar sesión con el usuario por defecto
            try {
                Log.d(TAG, "Intentando iniciar sesión con usuario por defecto")
                auth.signInWithEmailAndPassword(DEFAULT_EMAIL, DEFAULT_PASSWORD).await()
                Log.d(TAG, "Inicio de sesión exitoso con usuario por defecto")
                return true
            } catch (e: Exception) {
                // El usuario no existe, hay que crearlo
                Log.d(TAG, "Usuario por defecto no existe, creando uno nuevo: ${e.message}")
                try {
                    val result = auth.createUserWithEmailAndPassword(DEFAULT_EMAIL, DEFAULT_PASSWORD).await()
                    
                    // Guardar información adicional en Firestore
                    result.user?.let { user ->
                        Log.d(TAG, "Usuario creado exitosamente, guardando en Firestore")
                        saveUserToFirestore(user)
                    }
                    true
                } catch (e: Exception) {
                    Log.e(TAG, "Error al crear usuario por defecto: ${e.message}")
                    throw e
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error general en createDefaultUserIfNeeded: ${e.message}")
            false
        }
    }
    
    private suspend fun saveUserToFirestore(user: FirebaseUser) {
        try {
            val db = FirebaseFirestore.getInstance()
            val userData = hashMapOf(
                "nombre" to DEFAULT_NAME,
                "email" to DEFAULT_EMAIL,
                "id" to user.uid,
                "fechaRegistro" to System.currentTimeMillis()
            )
            
            db.collection("usuarios").document(user.uid).set(userData).await()
            Log.d(TAG, "Información de usuario guardada en Firestore")
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar en Firestore: ${e.message}")
            throw e
        }
    }
    
    fun getDefaultCredentials(): Pair<String, String> {
        return Pair(DEFAULT_EMAIL, DEFAULT_PASSWORD)
    }
}
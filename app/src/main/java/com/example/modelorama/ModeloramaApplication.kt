package com.example.modelorama

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class ModeloramaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Enable Firebase debug logging in debug mode
        val isDebugMode = true  // Change to false for production
        if (isDebugMode) {
            FirebaseFirestore.setLoggingEnabled(true)
        }
    }
}
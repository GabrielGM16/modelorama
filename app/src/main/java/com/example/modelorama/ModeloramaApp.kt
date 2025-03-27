package com.example.modelorama

import android.app.Application
import com.google.firebase.FirebaseApp

class ModeloramaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
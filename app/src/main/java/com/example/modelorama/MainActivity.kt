package com.example.modelorama

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            auth = FirebaseAuth.getInstance()
            
            // Get current user
            val user = auth.currentUser
            
            // Display welcome message
            user?.let {
                binding.textViewWelcome.text = "Bienvenido, ${it.email}"
            }
            
            // Set up logout button
            binding.buttonLogout.setOnClickListener {
                auth.signOut()
                finish()
            }
            
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
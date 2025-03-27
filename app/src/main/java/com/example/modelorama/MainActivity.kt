package com.example.modelorama

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.modelorama.databinding.ActivityMainBinding
import com.example.modelorama.model.Producto  // Updated import
import com.example.modelorama.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tag = "MainActivity"

    // Fix the BottomNavigationView type mismatch
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is logged in
        if (auth.currentUser == null) {
            // User is not logged in, redirect to login screen
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define appBarConfiguration as a class property, not a local variable
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_admin, R.id.navigation_profile)
        )
        

        
        // To match your actual fragment ID in your layout file, likely:
        // Replace the duplicate navController declarations with a single one
        // Around line 50-53
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // or use the correct ID for your nav host fragment
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Fix the navView reference
        // Fix the bottomNavigationView reference - make sure this matches your binding
        binding.navView.setupWithNavController(navController)
    }
    
    private fun saveProductToFirestore(producto: Producto) {
        db.collection("productos")
            .add(producto)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "Product saved with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error saving product", e)
            }
    }
}
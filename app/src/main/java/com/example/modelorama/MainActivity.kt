package com.example.modelorama

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.modelorama.databinding.ActivityMainBinding
import com.example.modelorama.ui.auth.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()
            
            // Set up the toolbar - make sure this is the correct ID from your layout
            setSupportActionBar(binding.appBarMain.toolbar)
            
            // Set up the drawer layout
            drawerLayout = binding.drawerLayout
            val navView: NavigationView = binding.navView
            
            // Make sure you have the correct nav host fragment ID
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            
            // Configure the navigation drawer
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home, R.id.nav_carrito, R.id.nav_promociones, R.id.nav_ubicaciones
                ), drawerLayout
            )
            
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
            navView.setNavigationItemSelectedListener(this)
            
            // Set up user info in navigation drawer header
            val headerView = navView.getHeaderView(0)
            val headerEmail = headerView.findViewById<android.widget.TextView>(R.id.textViewEmail)
            val headerName = headerView.findViewById<android.widget.TextView>(R.id.textViewName)
            
            // Get current user
            val user = auth.currentUser
            
            // Display user info
            user?.let {
                headerEmail.text = it.email
                // You might want to fetch the user's name from Firestore
                db.collection("usuarios").document(it.uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val nombre = document.getString("nombre")
                            if (nombre != null && nombre.isNotEmpty()) {
                                headerName.text = nombre
                            } else {
                                headerName.text = "Usuario Modelorama"
                            }
                        }
                    }
            }
            
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
            }
            R.id.nav_carrito -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_carrito)
            }
            R.id.nav_promociones -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_promociones)
            }
            R.id.nav_ubicaciones -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_ubicaciones)
            }
            R.id.nav_logout -> {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
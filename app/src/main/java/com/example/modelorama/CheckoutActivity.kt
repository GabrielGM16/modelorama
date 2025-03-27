package com.example.modelorama

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.databinding.ActivityCheckoutBinding
import com.example.modelorama.model.Producto
import com.example.modelorama.utils.QRCodeGenerator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        // Display cart total
        val total = CartManager.getCartTotal()
        binding.totalTextView.text = "Total: $${String.format("%.2f", total)}"
        
        binding.confirmButton.setOnClickListener {
            processOrder()
        }
    }
    
    private fun processOrder() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Por favor inicie sesión para continuar", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Create order object
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val orderDate = dateFormat.format(Date())
        val orderId = UUID.randomUUID().toString()
        
        val items = CartManager.getCartItems().map {
            hashMapOf(
                "productId" to it.id,
                "nombre" to it.nombre,
                "precio" to it.precio,
                "cantidad" to it.cantidad
            )
        }
        
        val order = hashMapOf(
            "orderId" to orderId,
            "userId" to userId,
            "fecha" to orderDate,
            "total" to CartManager.getCartTotal(),
            "items" to items
        )
        
        // Save order to Firestore
        db.collection("pedidos")
            .document(orderId)
            .set(order)
            .addOnSuccessListener {
                // Generate QR code with order details
                generateQRCode(orderId)
                
                // Clear cart
                CartManager.clearCart()
                
                Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al procesar la orden: ${e.message}", 
                    Toast.LENGTH_SHORT).show()
            }
    }
    
    private fun generateQRCode(orderId: String) {
        // Create QR code content
        val qrContent = "OrderID: $orderId\n" +
                "Fecha: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\n" +
                "Total: $${String.format("%.2f", CartManager.getCartTotal())}"
        
        // Generate QR code bitmap using your utility class
        val qrBitmap: Bitmap = QRCodeGenerator.generateQRCode(qrContent, 500, 500)
        
        // Display QR code
        binding.qrCodeImageView.setImageBitmap(qrBitmap)
    }
}
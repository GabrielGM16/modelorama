package com.example.modelorama

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.databinding.ActivityCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupUI()
        loadCartSummary()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Finalizar compra"

        binding.toolbar.setNavigationOnClickListener {
            // Reemplazamos onBackPressed() con el nuevo método recomendado
            onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonConfirmOrder.setOnClickListener {
            if (validateForm()) {
                processOrder()
            }
        }
    }

    private fun loadCartSummary() {
        val items = CartManager.items.value ?: emptyList()
        val totalItems = items.sumOf { it.cantidad }
        val totalPrice = items.sumOf { it.subtotal }

        binding.textViewItemCount.text = "Productos: $totalItems"
        binding.textViewSubtotal.text = "Subtotal: $ $totalPrice"
        binding.textViewTotal.text = "Total: $ $totalPrice"
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val name = binding.editTextName.text.toString().trim()
        val address = binding.editTextAddress.text.toString().trim()
        val phone = binding.editTextPhone.text.toString().trim()

        if (name.isEmpty()) {
            binding.editTextName.error = "Ingresa tu nombre"
            isValid = false
        }

        if (address.isEmpty()) {
            binding.editTextAddress.error = "Ingresa tu dirección"
            isValid = false
        }

        if (phone.isEmpty()) {
            binding.editTextPhone.error = "Ingresa tu teléfono"
            isValid = false
        }

        return isValid
    }

    private fun processOrder() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Debes iniciar sesión para realizar un pedido", Toast.LENGTH_LONG).show()
            return
        }

        binding.buttonConfirmOrder.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        val items = CartManager.items.value ?: emptyList()
        val orderItems = items.map {
            mapOf(
                "productId" to it.producto.id,
                "name" to it.producto.name,
                "price" to it.producto.price,
                "quantity" to it.cantidad,
                "subtotal" to it.subtotal
            )
        }

        val order = hashMapOf(
            "userId" to currentUser.uid,
            "userEmail" to currentUser.email,
            "userName" to binding.editTextName.text.toString().trim(),
            "address" to binding.editTextAddress.text.toString().trim(),
            "phone" to binding.editTextPhone.text.toString().trim(),
            "items" to orderItems,
            "totalItems" to items.sumOf { it.cantidad },
            "totalPrice" to items.sumOf { it.subtotal },
            "status" to "pendiente",
            "createdAt" to Date()
        )

        db.collection("pedidos")
            .add(order)
            .addOnSuccessListener { documentReference ->
                // Aquí abrimos la actividad de ticket con el ID del pedido
                val intent = Intent(this, TicketActivity::class.java)
                intent.putExtra("ORDER_ID", documentReference.id)
                intent.putExtra("CUSTOMER_NAME", binding.editTextName.text.toString().trim())
                intent.putExtra("CUSTOMER_ADDRESS", binding.editTextAddress.text.toString().trim())
                intent.putExtra("CUSTOMER_PHONE", binding.editTextPhone.text.toString().trim())
                intent.putExtra("TOTAL_ITEMS", items.sumOf { it.cantidad })
                intent.putExtra("TOTAL_PRICE", items.sumOf { it.subtotal })
                
                CartManager.clearCart()
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                binding.buttonConfirmOrder.isEnabled = true
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error al procesar el pedido: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
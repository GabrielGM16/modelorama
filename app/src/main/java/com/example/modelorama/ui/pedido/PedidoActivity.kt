package com.example.modelorama.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.databinding.ActivityPedidoBinding
import com.example.modelorama.model.Pedido
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class PedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidoBinding
    private lateinit var db: FirebaseFirestore
    private var pedidoId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        
        // Get pedido ID from intent
        pedidoId = intent.getStringExtra("PEDIDO_ID") ?: ""
        
        if (pedidoId.isNotEmpty()) {
            cargarPedido(pedidoId)
        } else {
            mostrarError("ID de pedido no válido")
        }
    }
    
    private fun cargarPedido(pedidoId: String) {
        db.collection("pedidos").document(pedidoId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Inside the cargarPedido method
                    val pedidoJson = Gson().toJson(document.data)
                    val pedido = Gson().fromJson(pedidoJson, Pedido::class.java)
                    mostrarPedido(pedido)
                } else {
                    mostrarError("Pedido no encontrado")
                }
            }
            .addOnFailureListener { exception ->
                mostrarError("Error al cargar el pedido: ${exception.message}")
            }
    }
    
    private fun mostrarPedido(pedido: Pedido) {
        binding.textViewFecha.text = "Fecha: ${pedido.fecha}"
        binding.textViewTotal.text = "Total: $${pedido.total}"
        binding.textViewEstado.text = "Estado: ${pedido.estado}"
        
        binding.buttonVerDetalles.setOnClickListener {
            binding.layoutDetalles.visibility = View.VISIBLE
        }
    }
    
    private fun mostrarError(mensaje: String) {
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = "Error: $mensaje"
    }
}
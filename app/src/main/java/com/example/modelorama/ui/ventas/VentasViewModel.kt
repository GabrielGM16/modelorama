package com.example.modelorama.ui.ventas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.modelorama.model.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VentasViewModel : ViewModel() {
    
    private val _historialVentas = MutableLiveData<List<Pedido>>()
    val historialVentas: LiveData<List<Pedido>> = _historialVentas
    
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    fun cargarHistorialVentas() {
        val userId = auth.currentUser?.uid ?: return
        
        db.collection("pedidos")
            .whereEqualTo("userId", userId)
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val pedidos = documents.mapNotNull { doc ->
                    doc.toObject(Pedido::class.java)
                }
                _historialVentas.value = pedidos
            }
    }
}
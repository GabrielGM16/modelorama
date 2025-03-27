package com.example.modelorama.data

import android.util.Log
import com.example.modelorama.ui.home.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val productosCollection = db.collection("productos")
    private val ventasCollection = db.collection("ventas")
    
    suspend fun getProductos(): List<Producto> = withContext(Dispatchers.IO) {
        try {
            val snapshot = productosCollection.get().await()
            return@withContext snapshot.documents.mapNotNull { document ->
                val producto = document.toObject(Producto::class.java)
                producto?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error getting productos", e)
            return@withContext emptyList()
        }
    }
    
    suspend fun addProducto(producto: Producto): Boolean = withContext(Dispatchers.IO) {
        try {
            productosCollection.add(producto).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error adding producto", e)
            return@withContext false
        }
    }
    
    suspend fun updateProducto(producto: Producto): Boolean = withContext(Dispatchers.IO) {
        try {
            productosCollection.document(producto.id).set(producto).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error updating producto", e)
            return@withContext false
        }
    }
    
    suspend fun deleteProducto(productoId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            productosCollection.document(productoId).delete().await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error deleting producto", e)
            return@withContext false
        }
    }
    
    suspend fun updateStock(productoId: String, newStock: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            productosCollection.document(productoId).update("stock", newStock).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error updating stock", e)
            return@withContext false
        }
    }
    
    suspend fun registrarVenta(venta: Map<String, Any>): Boolean = withContext(Dispatchers.IO) {
        try {
            ventasCollection.add(venta).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error registering sale", e)
            return@withContext false
        }
    }
    
    suspend fun getHistorialVentas(): List<Map<String, Any>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = ventasCollection.orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING).get().await()
            return@withContext snapshot.documents.map { it.data ?: mapOf() }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error getting sales history", e)
            return@withContext emptyList()
        }
    }
}
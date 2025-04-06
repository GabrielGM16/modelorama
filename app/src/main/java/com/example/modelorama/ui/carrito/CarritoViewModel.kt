package com.example.modelorama.ui.carrito

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.modelorama.model.CarritoItem
import com.example.modelorama.model.Product

class CarritoViewModel : ViewModel() {
    private val _items = MutableLiveData<List<CarritoItem>>(emptyList())
    val items: LiveData<List<CarritoItem>> = _items
    
    private val _totalItems = MutableLiveData(0)
    val totalItems: LiveData<Int> = _totalItems
    
    private val _totalPrice = MutableLiveData(0.0)
    val totalPrice: LiveData<Double> = _totalPrice
    
    fun addProduct(product: Product) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentItems.find { it.producto.id == product.id }
        
        if (existingItem != null) {
            // Si el producto ya está en el carrito, aumentamos la cantidad
            existingItem.cantidad++
        } else {
            // Si no, añadimos un nuevo item
            currentItems.add(CarritoItem(product))
        }
        
        _items.value = currentItems
        calculateTotals()
    }
    
    fun updateQuantity(productId: String, quantity: Int) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val item = currentItems.find { it.producto.id == productId }
        
        if (item != null) {
            item.cantidad = quantity
            _items.value = currentItems
            calculateTotals()
        }
    }
    
    fun removeProduct(productId: String) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val updatedItems = currentItems.filter { it.producto.id != productId }
        
        _items.value = updatedItems
        calculateTotals()
    }
    
    fun clearCart() {
        _items.value = emptyList()
        calculateTotals()
    }
    
    private fun calculateTotals() {
        val items = _items.value ?: emptyList()
        _totalItems.value = items.sumOf { it.cantidad }
        _totalPrice.value = items.sumOf { it.subtotal }
    }
    
    // Método para convertir un Map a un objeto Product
    fun mapToProduct(data: Map<String, Any>): Product {
        return Product(
            id = data["id"] as? String ?: "",
            name = data["name"] as? String ?: "",
            price = (data["price"] as? Number)?.toDouble() ?: 0.0,
            description = data["description"] as? String ?: "",
            imageUrl = data["imageUrl"] as? String ?: "",
            category = data["category"] as? String ?: "",
            stock = (data["stock"] as? Number)?.toInt() ?: 0
        )
    }
}
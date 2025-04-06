package com.example.modelorama

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modelorama.model.CarritoItem
import com.example.modelorama.model.Product

object CartManager {
    private val _items = MutableLiveData<List<CarritoItem>>(emptyList())
    val items: LiveData<List<CarritoItem>> = _items
    
    private val _totalItems = MutableLiveData(0)
    val totalItems: LiveData<Int> = _totalItems
    
    private val _totalPrice = MutableLiveData(0.0)
    val totalPrice: LiveData<Double> = _totalPrice
    
    fun addProduct(product: Product) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentItems.find { it.id == product.id }
        
        if (existingItem != null) {
            // Si el producto ya está en el carrito, aumentamos la cantidad
            existingItem.cantidad++
        } else {
            // Si no, añadimos un nuevo item
            currentItems.add(CarritoItem(product))
        }
        
        _items.value = currentItems
        updateTotals()
    }
    
    fun removeProduct(productId: String) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        currentItems.removeIf { it.id == productId }
        _items.value = currentItems
        updateTotals()
    }
    
    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeProduct(productId)
            return
        }
        
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val item = currentItems.find { it.id == productId }
        
        item?.let {
            it.cantidad = quantity
            _items.value = currentItems
            updateTotals()
        }
    }
    
    fun clearCart() {
        _items.value = emptyList()
        updateTotals()
    }
    
    private fun updateTotals() {
        val items = _items.value ?: emptyList()
        _totalItems.value = items.sumOf { it.cantidad }
        _totalPrice.value = items.sumOf { it.subtotal }
    }
}
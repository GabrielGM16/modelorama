package com.example.modelorama.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.modelorama.model.Producto

class HomeViewModel : ViewModel() {
    
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos
    
    private val _cart = MutableLiveData<List<Producto>>()
    val cart: LiveData<List<Producto>> = _cart
    
    init {
        // Initialize with empty lists or fetch from repository
        _productos.value = emptyList()
        _cart.value = emptyList()
    }
    
    fun addToCart(producto: Producto) {
        val currentCart = _cart.value?.toMutableList() ?: mutableListOf()
        currentCart.add(producto)
        _cart.value = currentCart
    }
}
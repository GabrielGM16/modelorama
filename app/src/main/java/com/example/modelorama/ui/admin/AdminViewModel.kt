package com.example.modelorama.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.modelorama.model.Producto

class AdminViewModel : ViewModel() {
    
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos
    
    init {
        // Initialize with empty list or fetch from repository
        _productos.value = emptyList()
    }
    
    fun deleteProducto(producto: Producto) {
        // Implement delete logic
        val currentList = _productos.value?.toMutableList() ?: mutableListOf()
        currentList.remove(producto)
        _productos.value = currentList
    }
}
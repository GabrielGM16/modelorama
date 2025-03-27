package com.example.modelorama.ui.carrito

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.modelorama.model.CarritoItem
import com.example.modelorama.model.Producto

class CarritoViewModel : ViewModel() {
    
    private val _items = MutableLiveData<List<CarritoItem>>(emptyList())
    val items: LiveData<List<CarritoItem>> = _items
    
    private val _total = MutableLiveData<Double>(0.0)
    val total: LiveData<Double> = _total
    
    fun agregarProducto(producto: Producto) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentItems.find { it.producto.id == producto.id }
        
        if (existingItem != null) {
            // Increase quantity if product already in cart
            existingItem.cantidad++
        } else {
            // Add new item to cart
            currentItems.add(CarritoItem(producto))
        }
        
        _items.value = currentItems
        calcularTotal()
    }
    
    fun eliminarProducto(productoId: String) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        currentItems.removeIf { it.producto.id == productoId }
        _items.value = currentItems
        calcularTotal()
    }
    
    fun actualizarCantidad(productoId: String, cantidad: Int) {
        if (cantidad <= 0) {
            eliminarProducto(productoId)
            return
        }
        
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val item = currentItems.find { it.producto.id == productoId }
        item?.let {
            it.cantidad = cantidad
            _items.value = currentItems
            calcularTotal()
        }
    }
    
    private fun calcularTotal() {
        val total = _items.value?.sumOf { it.subtotal } ?: 0.0
        _total.value = total
    }
    
    fun vaciarCarrito() {
        _items.value = emptyList()
        _total.value = 0.0
    }
    
    fun obtenerProductos(): List<Producto> {
        return _items.value?.map { it.producto } ?: emptyList()
    }

    fun incrementarCantidad(productoId: String) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val item = currentItems.find { it.producto.id == productoId }
        item?.let {
            it.cantidad++
            _items.value = currentItems
            calcularTotal()
        }
    }

    fun decrementarCantidad(productoId: String) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val item = currentItems.find { it.producto.id == productoId }
        item?.let {
            if (it.cantidad > 1) {
                it.cantidad--
                _items.value = currentItems
                calcularTotal()
            } else {
                eliminarItem(productoId)
            }
        }
    }

    fun eliminarItem(productoId: String) {
        eliminarProducto(productoId)
    }
}
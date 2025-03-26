package com.example.modelorama.ui.carrito

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.modelorama.model.CarritoItem
import com.example.modelorama.ui.home.Producto

class CarritoViewModel : ViewModel() {
    
    private val _items = MutableLiveData<MutableList<CarritoItem>>(mutableListOf())
    val items: LiveData<MutableList<CarritoItem>> = _items
    
    private val _total = MutableLiveData<Double>(0.0)
    val total: LiveData<Double> = _total
    
    fun agregarProducto(producto: Producto) {
        val carritoItems = _items.value ?: mutableListOf()
        val existingItem = carritoItems.find { it.producto.id == producto.id }
        
        if (existingItem != null) {
            existingItem.cantidad++
        } else {
            carritoItems.add(CarritoItem(producto))
        }
        
        // Importante: crear una nueva lista para que LiveData detecte el cambio
        _items.value = ArrayList(carritoItems)
        calcularTotal()
        
        // Agregar un log para depuración
        Log.d("CarritoViewModel", "Producto agregado: ${producto.nombre}, Total items: ${carritoItems.size}")
    }
    
    fun eliminarItem(position: Int) {
        val carritoItems = _items.value ?: mutableListOf()
        if (position >= 0 && position < carritoItems.size) {
            carritoItems.removeAt(position)
            _items.value = carritoItems
            calcularTotal()
        }
    }
    
    fun incrementarCantidad(position: Int) {
        val carritoItems = _items.value ?: mutableListOf()
        if (position >= 0 && position < carritoItems.size) {
            carritoItems[position].cantidad++
            _items.value = carritoItems
            calcularTotal()
        }
    }
    
    fun decrementarCantidad(position: Int) {
        val carritoItems = _items.value ?: mutableListOf()
        if (position >= 0 && position < carritoItems.size) {
            val item = carritoItems[position]
            if (item.cantidad > 1) {
                item.cantidad--
                _items.value = carritoItems
                calcularTotal()
            } else {
                eliminarItem(position)
            }
        }
    }
    
    fun vaciarCarrito() {
        _items.value = mutableListOf()
        _total.value = 0.0
    }
    
    private fun calcularTotal() {
        val carritoItems = _items.value ?: mutableListOf()
        _total.value = carritoItems.sumOf { it.subtotal }
    }
}
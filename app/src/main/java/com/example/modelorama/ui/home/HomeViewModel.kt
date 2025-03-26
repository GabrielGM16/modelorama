package com.example.modelorama.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _titulo = MutableLiveData<String>().apply {
        value = "Bienvenido a Modelorama"
    }
    val titulo: LiveData<String> = _titulo

    private val _productos = MutableLiveData<List<Producto>>().apply {
        value = listOf(
            Producto(
                id = "1",
                nombre = "Camiseta Basic", 
                descripcion = "Camiseta de algodón 100%", 
                precio = 19.99
            ),
            Producto(
                id = "2",
                nombre = "Jeans Slim Fit",
                descripcion = "Jeans ajustado color azul",
                precio = 49.99
            ),
            // Fixing the parameter order and types for these Producto instances
            Producto(
                id = "1", 
                nombre = "Cerveza Clara 355ml", 
                descripcion = "Cerveza clara en presentación individual", 
                precio = 25.0
            ),
            Producto(
                id = "2", 
                nombre = "Cerveza Oscura 355ml", 
                descripcion = "Cerveza oscura en presentación individual", 
                precio = 28.0
            ),
            Producto(
                id = "3", 
                nombre = "Six Pack Cerveza Clara", 
                descripcion = "Paquete de 6 cervezas claras", 
                precio = 120.0
            ),
            Producto(
                id = "4", 
                nombre = "Six Pack Cerveza Oscura", 
                descripcion = "Paquete de 6 cervezas oscuras", 
                precio = 135.0
            ),
            Producto(
                id = "5", 
                nombre = "Botana Mixta 150g", 
                descripcion = "Mezcla de botanas saladas", 
                precio = 45.0
            ),
            Producto(
                id = "6", 
                nombre = "Hielo 2kg", 
                descripcion = "Bolsa de hielo", 
                precio = 30.0
            )
        )
    }
    val productos: LiveData<List<Producto>> = _productos

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        // Simulación de productos disponibles en el Modelorama
        val listaProductos = listOf(
            Producto(
                id = "1", 
                nombre = "Cerveza Clara 355ml", 
                descripcion = "Cerveza clara en presentación individual", 
                precio = 25.0
            ),
            Producto(
                id = "2", 
                nombre = "Cerveza Oscura 355ml", 
                descripcion = "Cerveza oscura en presentación individual", 
                precio = 28.0
            ),
            Producto(
                id = "3", 
                nombre = "Six Pack Cerveza Clara", 
                descripcion = "Paquete de 6 cervezas claras", 
                precio = 120.0
            ),
            Producto(
                id = "4", 
                nombre = "Six Pack Cerveza Oscura", 
                descripcion = "Paquete de 6 cervezas oscuras", 
                precio = 135.0
            ),
            Producto(
                id = "5", 
                nombre = "Botana Mixta 150g", 
                descripcion = "Mezcla de botanas saladas", 
                precio = 45.0
            ),
            Producto(
                id = "6", 
                nombre = "Hielo 2kg", 
                descripcion = "Bolsa de hielo", 
                precio = 30.0
            )
        )
        _productos.value = listaProductos
    }
}
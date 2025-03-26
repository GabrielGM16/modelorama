package com.example.modelorama.model

import com.example.modelorama.ui.home.Producto

data class CarritoItem(
    val producto: Producto,
    var cantidad: Int = 1
) {
    val subtotal: Double
        get() = producto.precio * cantidad
}
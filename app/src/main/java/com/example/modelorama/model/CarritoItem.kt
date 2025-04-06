package com.example.modelorama.model

data class CarritoItem(
    val producto: Product,
    var cantidad: Int = 1,
    val id: String = producto.id
) {
    val subtotal: Double
        get() = producto.price * cantidad
}
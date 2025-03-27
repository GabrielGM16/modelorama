package com.example.modelorama.model

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val descripcion: String = "",
    val imagenUrl: String = "",
    val stock: Int = 0,
    val categoria: String = "",
    var cantidad: Int = 1  // For cart functionality
) {
    // Empty constructor for Firestore
    constructor() : this("", "", 0.0, "", "", 0, "")
    
    // Calculate subtotal for cart items
    val subtotal: Double
        get() = precio * cantidad
}
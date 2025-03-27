package com.example.modelorama.ui.home

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val imagenUrl: String = ""
) {
    // Empty constructor for Firestore
    constructor() : this("", "", "", 0.0, 0, "")
}
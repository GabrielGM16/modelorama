package com.example.modelorama.model

import com.example.modelorama.ui.home.Producto
import java.util.Date

data class Pedido(
    val id: String = "",
    val userId: String = "",
    val fecha: Date = Date(),
    val total: Double = 0.0,
    val estado: String = "Pendiente",
    val productos: List<ProductoPedido> = emptyList()
)

data class ProductoPedido(
    val productoId: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 0
)
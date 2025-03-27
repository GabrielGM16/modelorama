package com.example.modelorama.model

import com.example.modelorama.model.Producto
import java.util.Date

data class Pedido(
    val id: String = "",
    val userId: String = "",
    val fecha: Date = Date(),
    val productos: List<Producto> = emptyList(),
    val total: Double = 0.0,
    val estado: String = "pendiente"
)
package com.example.modelorama

import com.example.modelorama.model.Producto

object CartManager {
    private val cartItems = mutableListOf<Producto>()
    
    fun addToCart(product: Producto) {
        // Check if product already exists in cart
        val existingProduct = cartItems.find { it.id == product.id }
        if (existingProduct != null) {
            // Increase quantity
            existingProduct.cantidad += 1
        } else {
            // Add new product
            cartItems.add(product)
        }
    }
    
    fun removeFromCart(productId: String) {
        cartItems.removeIf { it.id == productId }
    }
    
    fun updateQuantity(productId: String, quantity: Int) {
        val product = cartItems.find { it.id == productId }
        product?.cantidad = quantity
    }
    
    fun getCartItems(): List<Producto> {
        return cartItems.toList()
    }
    
    fun getCartTotal(): Double {
        return cartItems.sumOf { it.precio * it.cantidad }
    }
    
    fun clearCart() {
        cartItems.clear()
    }
}
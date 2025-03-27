package com.example.modelorama.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modelorama.CartManager
import com.example.modelorama.R
import com.example.modelorama.model.Producto

class ProductAdapter(private val products: List<Producto>) : 
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImageView)
        val productName: TextView = view.findViewById(R.id.productNameTextView)
        val productPrice: TextView = view.findViewById(R.id.productPriceTextView)
        val addToCartButton: Button = view.findViewById(R.id.addToCartButton)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        
        holder.productName.text = product.nombre
        holder.productPrice.text = "$${String.format("%.2f", product.precio)}"
        
        // Load product image with Glide
        Glide.with(holder.itemView.context)
            .load(product.imagenUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.productImage)
        
        holder.addToCartButton.setOnClickListener {
            CartManager.addToCart(product)
            Toast.makeText(
                holder.itemView.context, 
                "${product.nombre} agregado al carrito", 
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    override fun getItemCount() = products.size
}
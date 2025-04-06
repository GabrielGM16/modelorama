package com.example.modelorama.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modelorama.R
import com.example.modelorama.models.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Aquí definimos correctamente las referencias a las vistas
        val productImageView: ImageView = itemView.findViewById(R.id.imageViewProduct)
        val productNameTextView: TextView = itemView.findViewById(R.id.textViewProductName)
        val productPriceTextView: TextView = itemView.findViewById(R.id.textViewProductPrice)
        val addToCartButton: Button = itemView.findViewById(R.id.buttonAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        
        // Cargar la imagen con Glide
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.productImageView)
        
        // Establecer los textos
        holder.productNameTextView.text = product.name
        holder.productPriceTextView.text = "$ ${product.price}"
        
        // Configurar los listeners
        holder.itemView.setOnClickListener { onItemClick(product) }
        holder.addToCartButton.setOnClickListener { onAddToCartClick(product) }
    }

    override fun getItemCount(): Int = products.size
}
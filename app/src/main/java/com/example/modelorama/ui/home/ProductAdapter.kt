package com.example.modelorama.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modelorama.CartManager
import com.example.modelorama.R
import com.example.modelorama.databinding.ItemProductBinding
import com.example.modelorama.model.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textViewProductName.text = product.name
            binding.textViewProductPrice.text = "$ ${product.price}"

            // Load image with Glide
            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.imageViewProduct)

            // Set click listeners
            binding.root.setOnClickListener { onProductClick(product) }
            binding.buttonAddToCart.setOnClickListener { 
                CartManager.addProduct(product)
                Toast.makeText(
                    binding.root.context, 
                    "${product.name} agregado al carrito", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
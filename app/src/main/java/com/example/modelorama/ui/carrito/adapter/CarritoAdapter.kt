package com.example.modelorama.ui.carrito.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modelorama.CartManager
import com.example.modelorama.R
import com.example.modelorama.databinding.ItemCarritoBinding
import com.example.modelorama.model.CarritoItem

class CarritoAdapter(
    private val items: List<CarritoItem>
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class CarritoViewHolder(private val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarritoItem) {
            val producto = item.producto
            
            binding.textViewProductName.text = producto.name  // Changed from nombre
            binding.textViewProductPrice.text = "$ ${producto.price}"  // Changed from precio
            binding.textViewQuantity.text = item.cantidad.toString()
            binding.textViewSubtotal.text = "$ ${item.subtotal}"
            
            // Load image with Glide
            Glide.with(binding.root.context)
                .load(producto.imageUrl)  // Changed from imagenUrl
                .placeholder(R.drawable.placeholder_image)  // Make sure this drawable exists
                .error(R.drawable.error_image)  // Make sure this drawable exists
                .into(binding.imageViewProduct)
                
            // Set click listeners
            binding.buttonIncrease.setOnClickListener {  // Changed from buttonIncrement
                CartManager.updateQuantity(item.id, item.cantidad + 1)
            }
            
            binding.buttonDecrease.setOnClickListener {  // Changed from buttonDecrement
                if (item.cantidad > 1) {
                    CartManager.updateQuantity(item.id, item.cantidad - 1)
                }
            }
            
            binding.buttonRemove.setOnClickListener {  // Changed from buttonDelete
                CartManager.removeProduct(item.id)
            }
        }
    }
}
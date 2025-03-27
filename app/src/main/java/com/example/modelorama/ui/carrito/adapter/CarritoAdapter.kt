package com.example.modelorama.ui.carrito.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modelorama.R
import com.example.modelorama.databinding.ItemCarritoBinding
import com.example.modelorama.model.CarritoItem

class CarritoAdapter(
    private val onIncrement: (String) -> Unit,
    private val onDecrement: (String) -> Unit,
    private val onDelete: (String) -> Unit
) : ListAdapter<CarritoItem, CarritoAdapter.CarritoViewHolder>(CarritoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CarritoViewHolder(private val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarritoItem) {
            val producto = item.producto
            
            binding.textViewProductName.text = producto.nombre
            binding.textViewProductPrice.text = "$${String.format("%.2f", producto.precio)}"
            binding.textViewQuantity.text = item.cantidad.toString()
            binding.textViewSubtotal.text = "$${String.format("%.2f", item.subtotal)}"
            
            // Load product image
            Glide.with(binding.root.context)
                .load(producto.imagenUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.imageViewProduct)
            
            // Set click listeners
            binding.buttonIncrement.setOnClickListener {
                onIncrement(producto.id)
            }
            
            binding.buttonDecrement.setOnClickListener {
                onDecrement(producto.id)
            }
            
            binding.buttonDelete.setOnClickListener {
                onDelete(producto.id)
            }
        }
    }

    class CarritoDiffCallback : DiffUtil.ItemCallback<CarritoItem>() {
        override fun areItemsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
            return oldItem.producto.id == newItem.producto.id
        }

        override fun areContentsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
            return oldItem == newItem
        }
    }
}
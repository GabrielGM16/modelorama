package com.example.modelorama.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.modelorama.R
import com.example.modelorama.model.Producto

class AdminProductosAdapter(
    private var productos: List<Producto> = emptyList(),
    private val onItemClick: (Producto) -> Unit,
    private val onDeleteClick: (Producto) -> Unit
) : RecyclerView.Adapter<AdminProductosAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Update these to match your actual layout IDs
        val nombreTextView: TextView = view.findViewById(R.id.textViewNombreProducto)
        val precioTextView: TextView = view.findViewById(R.id.textViewPrecio)
        val deleteButton: View = view.findViewById(R.id.buttonAddToCart) // or create a proper delete button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombreTextView.text = producto.nombre
        holder.precioTextView.text = "$ ${producto.precio}"
        
        holder.itemView.setOnClickListener { onItemClick(producto) }
        holder.deleteButton.setOnClickListener { onDeleteClick(producto) }
    }

    override fun getItemCount() = productos.size

    fun updateProductos(newProductos: List<Producto>) {
        productos = newProductos
        notifyDataSetChanged()
    }
}
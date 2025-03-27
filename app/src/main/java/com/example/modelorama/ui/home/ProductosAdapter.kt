package com.example.modelorama.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modelorama.R
import com.example.modelorama.model.Producto

class ProductosAdapter(
    private var productos: List<Producto> = emptyList(),
    private val onItemClick: (Producto) -> Unit,
    private val onAddToCartClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewProducto: ImageView = view.findViewById(R.id.imageViewProducto)
        val textViewNombreProducto: TextView = view.findViewById(R.id.textViewNombreProducto)
        val textViewPrecio: TextView = view.findViewById(R.id.textViewPrecio)
        val buttonAddToCart: Button = view.findViewById(R.id.buttonAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        
        holder.textViewNombreProducto.text = producto.nombre
        holder.textViewPrecio.text = "$ ${producto.precio}"
        
        // Load image with Glide if there's an image URL
        if (producto.imagenUrl.isNotEmpty()) {
            Glide.with(holder.imageViewProducto.context)
                .load(producto.imagenUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageViewProducto)
        }
        
        holder.itemView.setOnClickListener { onItemClick(producto) }
        holder.buttonAddToCart.setOnClickListener { onAddToCartClick(producto) }
    }

    override fun getItemCount() = productos.size

    fun updateProductos(newProductos: List<Producto>) {
        productos = newProductos
        notifyDataSetChanged()
    }
}
package com.example.modelorama.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.modelorama.R

class ProductosAdapter(private val onProductoClick: (Producto) -> Unit) : 
    RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {
    
    private var productos: List<Producto> = emptyList()
    
    fun actualizarProductos(nuevosProductos: List<Producto>) {
        productos = nuevosProductos
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(productos[position])
    }
    
    override fun getItemCount() = productos.size
    
    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(producto: Producto) {
            itemView.findViewById<TextView>(R.id.textViewNombreProducto).text = producto.nombre
            itemView.findViewById<TextView>(R.id.textViewPrecioProducto).text = "$${producto.precio}"
            itemView.findViewById<TextView>(R.id.textViewDescripcionProducto).text = producto.descripcion
            
            itemView.setOnClickListener {
                onProductoClick(producto)
            }
        }
    }
}
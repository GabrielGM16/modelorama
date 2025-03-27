package com.example.modelorama.ui.ventas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.modelorama.R
import com.example.modelorama.model.Pedido
import java.text.SimpleDateFormat
import java.util.Locale

class VentasAdapter(
    private var ventas: List<Pedido> = emptyList(),
    private val onItemClick: (Pedido) -> Unit
) : RecyclerView.Adapter<VentasAdapter.VentaViewHolder>() {

    class VentaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewFecha: TextView = view.findViewById(R.id.textViewFecha)
        val textViewTotal: TextView = view.findViewById(R.id.textViewTotal)
        val textViewEstado: TextView = view.findViewById(R.id.textViewEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = ventas[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        
        holder.textViewFecha.text = dateFormat.format(venta.fecha)
        holder.textViewTotal.text = "$ ${venta.total}"
        holder.textViewEstado.text = venta.estado
        
        holder.itemView.setOnClickListener { onItemClick(venta) }
    }

    override fun getItemCount() = ventas.size

    fun actualizarVentas(nuevasVentas: List<Pedido>) {
        ventas = nuevasVentas
        notifyDataSetChanged()
    }
}
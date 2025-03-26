package com.example.modelorama.ui.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.modelorama.R
import com.example.modelorama.databinding.FragmentCarritoBinding
import com.example.modelorama.model.CarritoItem
import java.text.NumberFormat
import java.util.Locale

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private lateinit var carritoViewModel: CarritoViewModel
    private lateinit var carritoAdapter: CarritoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        carritoViewModel = ViewModelProvider(requireActivity()).get(CarritoViewModel::class.java)

        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupObservers()
        setupButtons()

        return root
    }

    private fun setupRecyclerView() {
        carritoAdapter = CarritoAdapter(
            onIncrementarClick = { position ->
                carritoViewModel.incrementarCantidad(position)
            },
            onDecrementarClick = { position ->
                carritoViewModel.decrementarCantidad(position)
            },
            onEliminarClick = { position ->
                carritoViewModel.eliminarItem(position)
            }
        )

        binding.recyclerViewCarrito.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = carritoAdapter
        }
    }

    private fun setupObservers() {
        carritoViewModel.items.observe(viewLifecycleOwner) { items ->
            carritoAdapter.actualizarItems(items)
            binding.emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerViewCarrito.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
            binding.layoutResumen.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
        }

        carritoViewModel.total.observe(viewLifecycleOwner) { total ->
            val formato = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
            binding.textViewTotal.text = "Total: ${formato.format(total)}"
        }
    }

    private fun setupButtons() {
        binding.buttonVaciarCarrito.setOnClickListener {
            carritoViewModel.vaciarCarrito()
        }

        binding.buttonPagar.setOnClickListener {
            if (carritoViewModel.items.value?.isNotEmpty() == true) {
                // Aquí iría la lógica para proceder al pago
                Toast.makeText(context, "Procesando pago...", Toast.LENGTH_SHORT).show()
                // Simulamos un pago exitoso
                Toast.makeText(context, "¡Pago exitoso! Gracias por tu compra.", Toast.LENGTH_LONG).show()
                carritoViewModel.vaciarCarrito()
            } else {
                Toast.makeText(context, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CarritoAdapter(
    private val onIncrementarClick: (Int) -> Unit,
    private val onDecrementarClick: (Int) -> Unit,
    private val onEliminarClick: (Int) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    private var items: List<CarritoItem> = emptyList()

    fun actualizarItems(nuevosItems: List<CarritoItem>) {
        items = nuevosItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombreProductoCarrito)
        private val textViewPrecio: TextView = itemView.findViewById(R.id.textViewPrecioProductoCarrito)
        private val textViewCantidad: TextView = itemView.findViewById(R.id.textViewCantidad)
        private val textViewSubtotal: TextView = itemView.findViewById(R.id.textViewSubtotal)
        private val buttonIncrementar: Button = itemView.findViewById(R.id.buttonIncrementar)
        private val buttonDecrementar: Button = itemView.findViewById(R.id.buttonDecrementar)
        private val buttonEliminar: Button = itemView.findViewById(R.id.buttonEliminar)

        fun bind(item: CarritoItem, position: Int) {
            val formato = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
            
            textViewNombre.text = item.producto.nombre
            textViewPrecio.text = formato.format(item.producto.precio)
            textViewCantidad.text = item.cantidad.toString()
            textViewSubtotal.text = formato.format(item.subtotal)

            buttonIncrementar.setOnClickListener { onIncrementarClick(position) }
            buttonDecrementar.setOnClickListener { onDecrementarClick(position) }
            buttonEliminar.setOnClickListener { onEliminarClick(position) }
        }
    }
}
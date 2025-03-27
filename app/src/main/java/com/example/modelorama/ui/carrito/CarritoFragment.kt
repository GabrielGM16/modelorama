package com.example.modelorama.ui.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modelorama.databinding.FragmentCarritoBinding
import com.example.modelorama.model.CarritoItem
import com.example.modelorama.ui.carrito.adapter.CarritoAdapter

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
        carritoViewModel = ViewModelProvider(requireActivity())[CarritoViewModel::class.java]
        
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        
        setupRecyclerView()
        setupObservers()
        setupButtons()
        
        return root
    }
    
    private fun setupRecyclerView() {
        carritoAdapter = CarritoAdapter(
            onIncrement = { productoId ->
                carritoViewModel.incrementarCantidad(productoId)
            },
            onDecrement = { productoId ->
                carritoViewModel.decrementarCantidad(productoId)
            },
            onDelete = { productoId ->
                carritoViewModel.eliminarItem(productoId)
            }
        )
        
        binding.recyclerViewCarrito.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = carritoAdapter
        }
    }
    
    private fun setupObservers() {
        carritoViewModel.items.observe(viewLifecycleOwner) { items ->
            carritoAdapter.submitList(items)
            updateEmptyState(items)
        }
        
        carritoViewModel.total.observe(viewLifecycleOwner) { total ->
            binding.textViewTotal.text = "Total: $${String.format("%.2f", total)}"
        }
    }
    
    private fun updateEmptyState(items: List<CarritoItem>) {
        if (items.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.recyclerViewCarrito.visibility = View.GONE
            binding.checkoutLayout.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.recyclerViewCarrito.visibility = View.VISIBLE
            binding.checkoutLayout.visibility = View.VISIBLE
        }
    }
    
    private fun setupButtons() {
        binding.buttonCheckout.setOnClickListener {
            // Navigate to checkout
            // findNavController().navigate(R.id.action_carritoFragment_to_checkoutFragment)
        }
        
        binding.buttonVaciarCarrito.setOnClickListener {
            carritoViewModel.vaciarCarrito()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.modelorama.ui.carrito

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modelorama.CartManager
import com.example.modelorama.CheckoutActivity
import com.example.modelorama.databinding.FragmentCarritoBinding
import com.example.modelorama.model.CarritoItem

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarritoAdapter  // Changed from adapter.CarritoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeCartChanges()
        
        binding.buttonCheckout.setOnClickListener {
            if (CartManager.items.value?.isNotEmpty() == true) {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            }
        }
        
        binding.buttonClearCart.setOnClickListener {
            CartManager.clearCart()
        }
    }

    private fun setupRecyclerView() {
        adapter = CarritoAdapter(emptyList())  // Changed from adapter.CarritoAdapter
        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CarritoFragment.adapter
        }
    }
    
    private fun observeCartChanges() {
        CartManager.items.observe(viewLifecycleOwner) { items ->
            updateUI(items)
        }
        
        CartManager.totalPrice.observe(viewLifecycleOwner) { total ->
            binding.textViewTotal.text = "Total: $ $total"
        }
    }
    
    private fun updateUI(items: List<CarritoItem>) {
        adapter = CarritoAdapter(items)  // Changed from adapter.CarritoAdapter
        binding.recyclerViewCart.adapter = adapter
        
        if (items.isEmpty()) {
            binding.recyclerViewCart.visibility = View.GONE
            binding.layoutEmptyCart.visibility = View.VISIBLE
            binding.buttonCheckout.isEnabled = false
            binding.buttonClearCart.isEnabled = false
        } else {
            binding.recyclerViewCart.visibility = View.VISIBLE
            binding.layoutEmptyCart.visibility = View.GONE
            binding.buttonCheckout.isEnabled = true
            binding.buttonClearCart.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.modelorama.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modelorama.databinding.FragmentHomeBinding
import com.example.modelorama.model.Producto

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: ProductosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        adapter = ProductosAdapter(
            onItemClick = { producto ->
                // Handle item click
            },
            onAddToCartClick = { producto ->
                // Handle add to cart click
                homeViewModel.addToCart(producto)
            }
        )
        
        binding.recyclerViewProductos.adapter = adapter
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(context)
    }
    
    private fun observeViewModel() {
        homeViewModel.productos.observe(viewLifecycleOwner) { productos ->
            adapter.updateProductos(productos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
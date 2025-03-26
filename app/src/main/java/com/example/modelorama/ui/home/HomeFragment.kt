package com.example.modelorama.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.modelorama.R
import com.example.modelorama.databinding.FragmentHomeBinding
import com.example.modelorama.ui.carrito.CarritoViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productosAdapter: ProductosAdapter
    private lateinit var carritoViewModel: CarritoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        carritoViewModel = ViewModelProvider(requireActivity()).get(CarritoViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar el título
        val textView: TextView = binding.textHome
        homeViewModel.titulo.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Configurar el RecyclerView para mostrar productos
        setupRecyclerView()
        
        // Observar cambios en la lista de productos
        homeViewModel.productos.observe(viewLifecycleOwner) { productos ->
            productosAdapter.actualizarProductos(productos)
        }

        return root
    }

    private fun setupRecyclerView() {
        productosAdapter = ProductosAdapter { producto ->
            // Agregar producto al carrito
            carritoViewModel.agregarProducto(producto)
            Snackbar.make(
                binding.root,
                "Agregado al carrito: ${producto.nombre}",
                Snackbar.LENGTH_SHORT
            ).setAction("Ver Carrito") {
                // Navegar al fragmento del carrito
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.navigation_carrito)
            }.show()
        }
        
        binding.recyclerViewProductos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productosAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
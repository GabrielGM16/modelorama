package com.example.modelorama.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modelorama.databinding.FragmentAdminBinding
import com.example.modelorama.model.Producto

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var adapter: AdminProductosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        adapter = AdminProductosAdapter(
            onItemClick = { producto ->
                // Handle item click
            },
            onDeleteClick = { producto ->
                adminViewModel.deleteProducto(producto)
            }
        )
        
        binding.recyclerViewProductos.adapter = adapter
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(context)
        
        adminViewModel.productos.observe(viewLifecycleOwner) { productos ->
            adapter.updateProductos(productos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.modelorama.ui.ventas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modelorama.databinding.FragmentVentasHistorialBinding
import com.example.modelorama.ui.pedido.PedidoActivity

class VentasHistorialFragment : Fragment() {

    private var _binding: FragmentVentasHistorialBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var ventasViewModel: VentasViewModel
    private lateinit var adapter: VentasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ventasViewModel = ViewModelProvider(this)[VentasViewModel::class.java]
        _binding = FragmentVentasHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        ventasViewModel.cargarHistorialVentas()
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        adapter = VentasAdapter { pedido ->
            // Navigate to PedidoActivity with the selected pedido
            val intent = Intent(requireContext(), PedidoActivity::class.java)
            intent.putExtra("PEDIDO_ID", pedido.id)
            startActivity(intent)
        }
        
        binding.recyclerViewVentas.adapter = adapter
        binding.recyclerViewVentas.layoutManager = LinearLayoutManager(context)
    }
    
    private fun observeViewModel() {
        ventasViewModel.historialVentas.observe(viewLifecycleOwner) { ventas ->
            adapter.actualizarVentas(ventas)
            binding.textViewNoVentas.visibility = if (ventas.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
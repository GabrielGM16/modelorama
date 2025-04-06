package com.example.modelorama.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.modelorama.databinding.FragmentHomeBinding
import com.example.modelorama.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.example.modelorama.ui.auth.LoginActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        db = FirebaseFirestore.getInstance()
        
        setupRecyclerView()
        loadProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            productList,
            onProductClick = { product ->
                // Handle product click (show details)
                Toast.makeText(context, "Producto: ${product.name}", Toast.LENGTH_SHORT).show()
            },
            onAddToCartClick = { product ->
                // Handle add to cart
                Toast.makeText(context, "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()
            }
        )
        
        binding.recyclerViewProducts.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun loadProducts() {
        binding.progressBar.visibility = View.VISIBLE
        
        // Check if user is logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // Redirect to login if not logged in
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }
        
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                productList.clear()
                for (document in result) {
                    val product = document.toObject(Product::class.java).copy(id = document.id)
                    productList.add(product)
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
                
                if (productList.isEmpty()) {
                    binding.textViewNoProducts.visibility = View.VISIBLE
                } else {
                    binding.textViewNoProducts.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error getting products", exception)
                binding.progressBar.visibility = View.GONE
                binding.textViewNoProducts.visibility = View.VISIBLE
                
                if (exception.message?.contains("PERMISSION_DENIED") == true) {
                    binding.textViewNoProducts.text = "Error de permisos: No tienes acceso a los productos. Por favor, inicia sesión nuevamente."
                    // Optionally redirect to login
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                } else {
                    binding.textViewNoProducts.text = "Error al cargar productos: ${exception.message}"
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
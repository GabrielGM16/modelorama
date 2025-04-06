package com.example.modelorama.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.modelorama.CartManager
import com.example.modelorama.databinding.FragmentHomeBinding
import com.example.modelorama.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProducts()
    }

    private fun loadProducts() {
        binding.progressBar.visibility = View.VISIBLE
        
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                val productList = mutableListOf<Product>()
                for (document in result) {
                    val product = document.toObject(Product::class.java).copy(id = document.id)
                    productList.add(product)
                }
                
                setupRecyclerView(productList)
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
                binding.textViewNoProducts.text = "Error al cargar productos: ${exception.message}"
            }
    }

    private fun setupRecyclerView(products: List<Product>) {
        val adapter = ProductAdapter(
            products,
            onProductClick = { product ->
                // Manejar clic en producto
            }
        )
        
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewProducts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.modelorama.ui.promociones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.modelorama.databinding.FragmentPromocionesBinding

class PromocionesFragment : Fragment() {

    private var _binding: FragmentPromocionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val promocionesViewModel = ViewModelProvider(this).get(PromocionesViewModel::class.java)

        _binding = FragmentPromocionesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPromociones
        promocionesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
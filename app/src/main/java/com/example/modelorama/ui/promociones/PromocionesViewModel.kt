package com.example.modelorama.ui.promociones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PromocionesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Próximamente: Promociones especiales"
    }
    val text: LiveData<String> = _text
}
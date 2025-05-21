package com.example.myapplication.ui.viewmodel

// package com.example.myapplication.viewmodel;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.network.ApiService
import com.example.myapplication.ui.DetalleProducto.DetalleProductoViewModel

class DetalleProductoViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalleProductoViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
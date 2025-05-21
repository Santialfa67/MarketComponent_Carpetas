package com.example.myapplication.ui.DetalleProducto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Producto
import com.example.myapplication.network.ApiService
import kotlinx.coroutines.launch

class DetalleProductoViewModel(private val apiService: ApiService) : ViewModel() {
    private val _detalleProducto = MutableLiveData<Producto>()
    val detalleProducto: LiveData<Producto> = _detalleProducto
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun obtenerDetalleProducto(productoId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val productoDetalle = apiService.obtenerDetalleProducto(productoId)
                _detalleProducto.value = productoDetalle
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Error al obtener detalles del producto: ${e.localizedMessage}"
                _isLoading.value = false
            }
        }
    }
}
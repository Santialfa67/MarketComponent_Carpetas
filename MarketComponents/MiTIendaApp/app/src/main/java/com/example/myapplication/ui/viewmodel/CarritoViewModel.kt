package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.CarritoManager
import com.example.myapplication.data.request.PedidoRequest
import com.example.myapplication.network.ApiService
import kotlinx.coroutines.launch

class CarritoViewModel(private val apiService: ApiService) : ViewModel() {

    private val _checkoutResult = MutableLiveData<Boolean>()
    val checkoutResult: LiveData<Boolean> = _checkoutResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun realizarPago() {
        _checkoutResult.value = false
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val itemsParaPedido = CarritoManager.carritoItems.value?.map {
                    PedidoRequest.ItemPedido(it.producto.producto_id, it.cantidad)
                } ?: emptyList()

                if (itemsParaPedido.isNotEmpty()) {
                    val pedidoRequest = PedidoRequest(itemsParaPedido)
                    val response = apiService.procesarPedido(pedidoRequest)

                    if (response.isSuccessful) {
                        _checkoutResult.value = true
                        CarritoManager.vaciarCarrito()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        _errorMessage.value = "Error al procesar el pago: ${errorBody ?: response.message()}"
                        _checkoutResult.value = false
                    }
                } else {
                    // Si llega aquí, significa que itemsParaPedido está vacío.
                    _errorMessage.value = "El carrito está vacío. Añade productos para proceder al pago."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión o inesperado: ${e.localizedMessage}"
                _checkoutResult.value = false
            }
        }
    }
}
// Archivo: com.example.myapplication.viewmodel.CarritoViewModel.kt
package com.example.myapplication.ui.Carrito

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.CarritoManager
import com.example.myapplication.network.ApiService
import com.example.myapplication.data.request.PedidoRequest
import com.google.gson.Gson // Importa Gson para parsear JSON
import com.example.myapplication.model.ErrorResponse // Importa tu nuevo DTO de error
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
                        _errorMessage.value = "¡Pedido procesado con éxito!" // Mensaje de éxito claro
                    } else {
                        // Cuando la respuesta NO es exitosa, intenta leer y parsear el errorBody
                        val errorBodyString = response.errorBody()?.string()
                        if (!errorBodyString.isNullOrEmpty()) {
                            try {
                                val errorResponse = Gson().fromJson(errorBodyString, ErrorResponse::class.java)
                                // Muestra el mensaje más relevante del error JSON
                                _errorMessage.value = "Error: ${errorResponse?.message ?: "Desconocido"}"
                            } catch (jsonParseError: Exception) {
                                // Si no se puede parsear como JSON, muestra el string completo
                                _errorMessage.value = "Error al procesar el pago: $errorBodyString"
                            }
                        } else {
                            // Si el errorBody está vacío, usa el mensaje de la respuesta HTTP
                            _errorMessage.value = "Error al procesar el pago: ${response.message()}"
                        }
                        _checkoutResult.value = false
                    }
                } else {
                    _errorMessage.value = "El carrito está vacío. Añade productos para proceder al pago."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión o inesperado: ${e.localizedMessage}"
                _checkoutResult.value = false
            }
        }
    }
}
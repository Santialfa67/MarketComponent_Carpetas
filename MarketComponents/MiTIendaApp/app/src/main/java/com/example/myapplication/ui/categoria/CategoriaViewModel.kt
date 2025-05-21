package com.example.myapplication.ui.categoria

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Categoria
import com.example.myapplication.model.Producto
import com.example.myapplication.network.CategoriaService // Importa el nuevo servicio
import com.example.myapplication.network.ProductoService
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.launch

class CategoriaViewModel : ViewModel() {
    private val categoriaService = CategoriaService()
    private val productoService = ProductoService()

    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

    private val _productosDeCategoria = MutableLiveData<List<Producto>>()
    val productosDeCategoria: LiveData<List<Producto>> = _productosDeCategoria

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoadingCategorias = MutableLiveData<Boolean>()
    val isLoadingCategorias: LiveData<Boolean> = _isLoadingCategorias

    private val _isLoadingProductos = MutableLiveData<Boolean>()
    val isLoadingProductos: LiveData<Boolean> = _isLoadingProductos

    init {
        obtenerCategorias()
    }

    fun obtenerCategorias() {

        _isLoadingCategorias.value = true

        viewModelScope.launch {
            try {
                val categoriasResponse = categoriaService.obtenerCategorias()
                Log.d("CategoriaViewModel", "Categorías recibidas: ${categoriasResponse.joinToString { "${it.nombre} (${it.categoria_id})" }}")
                _categorias.value = categoriasResponse

                _isLoadingCategorias.value = false
            } catch (e: Exception) {
                _error.value = "Error al obtener las categorías: ${e.localizedMessage}"
                _isLoadingCategorias.value = false
            }
        }
    }



    fun obtenerProductosPorCategoria(categoriaId: Int) {
        _isLoadingProductos.value = true
        viewModelScope.launch {
            try {
                val productosResponse = productoService.obtenerProductosPorCategoria(categoriaId)
                _productosDeCategoria.value = productosResponse
                _isLoadingProductos.value = false
            } catch (e: Exception) {
                _error.value = "Error al obtener los productos: ${e.localizedMessage}"
                _isLoadingProductos.value = false
            }
        }
    }

    // ¡NUEVO MÉTODO! Para obtener TODOS los productos
    fun obtenerTodosLosProductos() {
        _isLoadingProductos.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getProductos() // Asumo que tienes este método en tu ApiService
                if (response.isSuccessful) {
                    _productosDeCategoria.value = response.body()
                } else {
                    _error.value = "Error al obtener todos los productos: ${response.code()}"
                    Log.e("CategoriaViewModel", "Error al obtener todos los productos: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión al obtener todos los productos: ${e.message}"
                Log.e("CategoriaViewModel", "Excepción al obtener todos los productos", e)
            } finally {
                _isLoadingProductos.value = false
            }
        }
    }
}
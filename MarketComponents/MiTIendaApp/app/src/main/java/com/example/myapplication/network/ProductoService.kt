package com.example.myapplication.network

import com.example.myapplication.model.Producto

class ProductoService {
    private val apiService = RetrofitClient.instance

    suspend fun obtenerProductosPorCategoria(categoriaId: Int): List<Producto> {
        return apiService.obtenerProductosPorCategoria(categoriaId)
    }
}
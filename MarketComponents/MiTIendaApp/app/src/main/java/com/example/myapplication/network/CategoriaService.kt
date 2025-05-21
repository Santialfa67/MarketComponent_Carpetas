package com.example.myapplication.network

import com.example.myapplication.model.Categoria

//class CategoriaService {
//    private val apiService = RetrofitClient.instance
//    private val apiKey =
//        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9wdmJubXpzYnZvaWVlbHFkb2dyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc1MzE5MTIsImV4cCI6MjA2MzEwNzkxMn0.Kaegb6wRJmEPiYl3TIbxYKDQUZTPHhY2RMP-eMRLm3c" // Reemplaza con tu clave anónima de Supabase
//
//    suspend fun obtenerCategorias(): List<Categoria> {
//        return apiService.obtenerCategorias(
//            apiKey = apiKey,
//            authorization = bearerToken
//
//        )
//    }
//    suspend fun obtenerCategorias(): List<Categoria> {
//        return apiService.obtenerCategorias(apiKey = apiKey)
//    }
class CategoriaService {
    private val apiService = RetrofitClient.instance

    suspend fun obtenerCategorias(): List<Categoria> {
        return apiService.obtenerCategorias()
    }
}

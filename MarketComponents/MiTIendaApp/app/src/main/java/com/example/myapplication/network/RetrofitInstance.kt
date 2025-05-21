package com.example.myapplication.network

import com.example.myapplication.network.CategoriaService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //private const val BASE_URL = "https://opvbnmzsbvoieelqdogr.supabase.co" // Reemplaza con la URL base de tu API
    private const val BASE_URL ="http://10.0.2.2:8080/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val categoriaService: CategoriaService by lazy {
        retrofit.create(CategoriaService::class.java)
    }
}
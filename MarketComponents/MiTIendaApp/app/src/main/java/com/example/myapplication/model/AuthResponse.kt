package com.example.myapplication.model // Puedes ponerlo en .model o crear .data.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val message: String,
    val token: String? = null,
    @SerializedName("userId") // Asegúrate de que el nombre del campo JSON coincida
    val userId: Int? = null,
    val email: String? = null
)
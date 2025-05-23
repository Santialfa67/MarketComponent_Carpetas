// com.example.myapplication.model/ErrorResponse.kt
package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class ErrorResponseU(
    @SerializedName("message") val message: String, // Asegúrate que el nombre del campo en JSON sea "message"
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("status") val status: Int? = null
)
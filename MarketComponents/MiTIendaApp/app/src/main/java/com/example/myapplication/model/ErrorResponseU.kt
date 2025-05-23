package com.example.myapplication.model // O .data.response

data class ErrorResponseU(
    val message: String,
    val timestamp: String? = null,
    val status: Int? = null
)
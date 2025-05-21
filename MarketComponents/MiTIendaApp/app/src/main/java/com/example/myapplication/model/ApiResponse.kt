// Archivo: com.example.myapplication.model.ApiResponse.kt (o similar)
package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiResponse(
    val success: Boolean,
    val message: String
) : Parcelable
// com.example.myapplication.model/AuthResponse.kt
package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

//data class AuthResponse(
//    val message: String,
//    val token: String?,
//    val email: String?,
//    @SerializedName("id") // Asegúrate de que el nombre del campo en tu JSON coincida (ej. "id" o "userId")
//    val userId: Int?,
//    @SerializedName("nombre")
//    val userName: String?,
//    @SerializedName("telefono")
//    val userPhone: String?,
//    @SerializedName("direccion")
//    val userAddress: String?
//    // Puedes añadir más campos si tu API los devuelve, como roles, etc.
//)


data class AuthResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("token") val token: String?,
    @SerializedName("userId") val userId: Int?,    // <-- ¡Este nombre debe coincidir con el JSON!
    @SerializedName("email") val email: String?,
    @SerializedName("nombre") val userName: String?, // <-- Este nombre debe coincidir con el JSON!
    @SerializedName("telefono") val userPhone: String?, // <-- Este nombre debe coincidir con el JSON!
    @SerializedName("direccion") val userAddress: String? // <-- Este nombre debe coincidir con el JSON!
)


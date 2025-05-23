// com.example.myapplication.model/Usuario.kt
package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("id")
    val userId: Int?,
    @SerializedName("nombre")
    val nombre: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("password") // Se mantiene, ya que tu backend podría requerirlo para ciertas operaciones o deserialización.
    val password: String?,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("direccion")
    val direccion: String? // Mantenemos la dirección si la quieres para el perfil, aunque no la uses en otros lugares.
)
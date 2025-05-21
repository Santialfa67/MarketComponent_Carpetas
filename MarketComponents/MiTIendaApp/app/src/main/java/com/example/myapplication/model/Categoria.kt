package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
// Asegúrate de importar esto si usas @SerializedName para otros campos, aunque para categoria_id no se necesite
// import com.google.gson.annotations.SerializedName

@Parcelize
data class Categoria(
    // No necesitas @SerializedName aquí porque el JSON ya usa "categoria_id"
    val categoria_id: Int,
    val nombre: String,
    val descripcion: String? // El JSON que mostraste tiene "descripcion", hazlo nullable por si acaso
) : Parcelable

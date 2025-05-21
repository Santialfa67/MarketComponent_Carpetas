package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarritoItem(
    val producto: Producto, // El objeto Producto completo
    var cantidad: Int // La cantidad de este producto en el carrito
) : Parcelable
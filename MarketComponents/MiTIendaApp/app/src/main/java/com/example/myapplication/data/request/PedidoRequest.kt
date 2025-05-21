package com.example.myapplication.data.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PedidoRequest(
    val items: List<ItemPedido>
) : Parcelable {
    @Parcelize
    data class ItemPedido(
        val productoId: Int,
        val cantidad: Int
    ) : Parcelable
}
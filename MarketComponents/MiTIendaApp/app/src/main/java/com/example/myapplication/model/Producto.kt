package com.example.myapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize

//data class Producto(
//    @SerializedName("productoId")
//    val producto_id: Int,
//    val nombre: String,
//    val descripcion: String,
//    val precio: Double,
//    val stock: Int,
//    val imagen: String?,
//    val proveedor: Proveedor?
//) : Parcelable
data class Producto(
    @SerializedName("productoId")
    val producto_id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int, // Este es el stock disponible, no la cantidad que el usuario selecciona
    val imagen: String?,
    val proveedor: Proveedor?,
    var cantidadSeleccionada: Int = 1 // ¡NUEVA PROPIEDAD! Con valor por defecto 1
) : Parcelable




//@Parcelize
//data class Proveedor(
//    val id: Int,
//    val nombre: String
//) : Parcelable

@Parcelize
data class Proveedor(
    @SerializedName("proveedorId")
    val proveedor_id: Int,
    val nombre: String,
    val contacto: String?,
    val direccion: String?,
    @SerializedName("productosOfrecidos")
    val productos_ofrecidos: String?
) : Parcelable
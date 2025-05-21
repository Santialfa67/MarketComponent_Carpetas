// Archivo: com.example.myapplication.CarritoManager.kt
package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.CarritoItem
import com.example.myapplication.model.Producto

object CarritoManager {

    private val _carritoItems = MutableLiveData<MutableList<CarritoItem>>(mutableListOf())
    val carritoItems: LiveData<MutableList<CarritoItem>> = _carritoItems

    private val _totalCarrito = MutableLiveData<Double>(0.0)
    val totalCarrito: LiveData<Double> = _totalCarrito

    fun agregarProductoAlCarrito(producto: Producto) {
        val currentItems = _carritoItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.producto.producto_id == producto.producto_id }

        if (existingItem != null) {
            // Si el producto ya está en el carrito, aumenta su cantidad
            // Ahora sumamos la cantidadSeleccionada del producto que se está añadiendo
            existingItem.cantidad += producto.cantidadSeleccionada
        } else {
            // Si es un producto nuevo, lo añade a la lista con la cantidadSeleccionada
            // del producto que llega del detalle
            currentItems.add(CarritoItem(producto, producto.cantidadSeleccionada))
        }
        _carritoItems.value = currentItems.toMutableList()
        calcularTotal()
    }

    fun removerProductoDelCarrito(producto: Producto) {
        val currentItems = _carritoItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.producto.producto_id == producto.producto_id }

        existingItem?.let {
            it.cantidad-- // Reduce la cantidad en 1 por cada llamada
            if (it.cantidad <= 0) {
                currentItems.remove(it)
            }
        }
        _carritoItems.value = currentItems.toMutableList()
        calcularTotal()
    }

    // Puedes mantener esta función si la usas para actualizaciones directas (ej. desde el carrito)
    fun actualizarCantidad(producto: Producto, newQuantity: Int) {
        val currentItems = _carritoItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.producto.producto_id == producto.producto_id }

        existingItem?.let {
            it.cantidad = newQuantity
            if (newQuantity <= 0) {
                currentItems.remove(it)
            }
        }
        _carritoItems.value = currentItems.toMutableList()
        calcularTotal()
    }

    fun vaciarCarrito() {
        _carritoItems.value = mutableListOf()
        calcularTotal()
    }

    private fun calcularTotal() {
        val total = (_carritoItems.value ?: mutableListOf()).sumOf { it.producto.precio * it.cantidad }
        _totalCarrito.value = total
    }
}
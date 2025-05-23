package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.CarritoItem
import com.example.myapplication.model.Producto
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso

class CarritoAdapter(
    private val onQuantityChange: (Producto, Int) -> Unit, // (producto, nuevaCantidad)
    private val onRemoveItem: (Producto) -> Unit // (producto) cuando la cantidad es 0 o menos
) : ListAdapter<CarritoItem, CarritoAdapter.CarritoViewHolder>(CarritoItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val carritoItem = getItem(position)
        holder.bind(carritoItem)
    }

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewCarritoItem: ImageView = itemView.findViewById(R.id.imageViewCarritoItem)
        private val textViewNombreCarritoItem: TextView = itemView.findViewById(R.id.textViewNombreCarritoItem)
        private val textViewPrecioCarritoItem: TextView = itemView.findViewById(R.id.textViewPrecioCarritoItem) // <-- CORREGIDA
        private val textViewCantidadCarritoItem: TextView = itemView.findViewById(R.id.textViewCantidadCarritoItem)
        private val buttonDisminuirCantidad: MaterialButton = itemView.findViewById(R.id.buttonDisminuirCantidad) // Cambiar a MaterialButton
        private val buttonAumentarCantidad: MaterialButton = itemView.findViewById(R.id.buttonAumentarCantidad) // Cambiar a MaterialButton
        private val textViewSubtotalItem: TextView = itemView.findViewById(R.id.textViewSubtotalItem) // Nuevo TextView

        fun bind(item: CarritoItem) {
            textViewNombreCarritoItem.text = item.producto.nombre
            textViewPrecioCarritoItem.text = "$${String.format("%.2f", item.producto.precio)}"
            textViewCantidadCarritoItem.text = item.cantidad.toString()

            // Cargar imagen con Picasso
            item.producto.imagen?.let { imageUrl ->
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground) // Placeholder
                    .error(R.drawable.ic_launcher_background) // Error
                    .into(imageViewCarritoItem)
            } ?: run {
                imageViewCarritoItem.setImageResource(R.drawable.ic_launcher_background)
            }

            // Calcular y mostrar el subtotal de este item
            val subtotal = item.producto.precio * item.cantidad
            textViewSubtotalItem.text = "$${String.format("%.2f", subtotal)}"


            buttonDisminuirCantidad.setOnClickListener {
                var newQuantity = item.cantidad - 1
                if (newQuantity < 0) newQuantity = 0 // Evitar cantidades negativas
                onQuantityChange(item.producto, newQuantity)
                if (newQuantity == 0) {
                    onRemoveItem(item.producto) // Llamar para remover si la cantidad llega a 0
                }
            }

            buttonAumentarCantidad.setOnClickListener {
                val newQuantity = item.cantidad + 1
                onQuantityChange(item.producto, newQuantity)
            }
        }
    }

    class CarritoItemDiffCallback : DiffUtil.ItemCallback<CarritoItem>() {
        override fun areItemsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
            return oldItem.producto.producto_id == newItem.producto.producto_id // Comparar por ID del producto
        }

        override fun areContentsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
            // Comparar si el contenido es el mismo (nombre, precio y cantidad son suficientes)
            return oldItem.producto.nombre == newItem.producto.nombre &&
                    oldItem.producto.precio == newItem.producto.precio &&
                    oldItem.cantidad == newItem.cantidad
        }
    }
}
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
import com.squareup.picasso.Picasso

class CarritoAdapter(
    private val onQuantityChanged: (CarritoItem, Int) -> Unit
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
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewCarritoItem)
        private val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreCarritoItem)
        private val precioTextView: TextView = itemView.findViewById(R.id.textViewPrecioCarritoItem)
        private val cantidadTextView: TextView = itemView.findViewById(R.id.textViewCantidadCarritoItem)
        private val btnDisminuir: Button = itemView.findViewById(R.id.buttonDisminuirCantidad)
        private val btnAumentar: Button = itemView.findViewById(R.id.buttonAumentarCantidad)

        fun bind(item: CarritoItem) {
            nombreTextView.text = item.producto.nombre
            precioTextView.text = String.format("$%.2f", item.producto.precio)
            cantidadTextView.text = item.cantidad.toString()

            item.producto.imagen?.let { imageUrl ->
                Picasso.get().load(imageUrl).into(imageView)
            } ?: imageView.setImageResource(R.drawable.ic_launcher_background)

            btnDisminuir.setOnClickListener {
                onQuantityChanged(item, item.cantidad - 1)
            }

            btnAumentar.setOnClickListener {
                onQuantityChanged(item, item.cantidad + 1)
            }
        }
    }
}

class CarritoItemDiffCallback : DiffUtil.ItemCallback<CarritoItem>() {
    override fun areItemsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
        // Los ítems son los mismos si sus IDs de producto coinciden
        return oldItem.producto.producto_id == newItem.producto.producto_id
    }

    override fun areContentsTheSame(oldItem: CarritoItem, newItem: CarritoItem): Boolean {
        // Los contenidos son los mismos si el objeto CarritoItem es el mismo (data class equals)
        // Esto funciona bien si CarritoItem es un 'data class' en Kotlin,
        // ya que la igualdad se basa en todos sus propiedades.
        return oldItem == newItem
    }
}
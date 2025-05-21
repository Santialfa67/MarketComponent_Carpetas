package com.example.myapplication


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetalleProductoActivity
import com.example.myapplication.R
import com.example.myapplication.model.Producto
import com.squareup.picasso.Picasso

class ProductoAdapter(
    private var listaDeProductos: List<Producto>,
    private val itemClickListener: OnItemClickListener // Puedes mantenerlo si lo usas en otro lugar
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(producto: Producto)
    }

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreProducto)
        val precioTextView: TextView = itemView.findViewById(R.id.textViewPrecioProducto)
        val imagenImageView: ImageView = itemView.findViewById(R.id.imageViewProducto)

        fun bind(producto: Producto) {
            nombreTextView.text = producto.nombre
            precioTextView.text = "$${String.format("%.2f", producto.precio)}" // Formatear el precio
            producto.imagen?.let { Picasso.get().load(it).into(imagenImageView) }
                ?: imagenImageView.setImageResource(R.drawable.ic_launcher_background) // Imagen por defecto
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        // Accede al producto usando la lista directamente
        val productoActual = listaDeProductos[position] // <-- ¡Cambio aquí!

        holder.bind(productoActual) // Pasa el producto actual al ViewHolder para que lo muestre

        holder.itemView.setOnClickListener {
            // Asegúrate de que estás usando el productoActual correcto
            Log.d("ProductoAdapter", "Producto clickeado: ID=${productoActual.producto_id}, Nombre=${productoActual.nombre}")
            val intent = Intent(holder.itemView.context, DetalleProductoActivity::class.java)
            intent.putExtra("producto", productoActual) // Asegúrate de que Producto es Parcelable
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = listaDeProductos.size

    fun actualizarProductos(nuevaLista: List<Producto>) {
        listaDeProductos = nuevaLista
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
}
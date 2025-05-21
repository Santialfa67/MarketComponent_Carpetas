package com.example.myapplication.ui.categoria

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Categoria

class CategoriaAdapter(
    private var listaDeCategorias: List<Categoria>,
    private val onItemClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreCategoria)

        fun bind(categoria: Categoria) {
            nombreTextView.text = categoria.nombre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoriaActual = listaDeCategorias[position]
        holder.bind(categoriaActual)
        holder.itemView.setOnClickListener {
            Log.d("CategoriaAdapter", "Categoría clickeada: ${categoriaActual.nombre},  ID: ${categoriaActual.categoria_id}")
            onItemClick(categoriaActual)
        }
    }

    override fun getItemCount() = listaDeCategorias.size

    // Método para actualizar la lista de categorías (útil si los datos cambian)
    fun actualizarCategorias(nuevaLista: List<Categoria>) {
        Log.d("CategoriaAdapter", "Lista de categorías actualizada: ${nuevaLista.joinToString { it.nombre }}")
        listaDeCategorias = nuevaLista
        notifyDataSetChanged()
    }
}
// Archivo: com.example.myapplication.CarritoActivity.kt
package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.network.RetrofitClient // Asegúrate de esta ruta
import com.example.myapplication.ui.Carrito.CarritoViewModel
import com.example.myapplication.ui.viewmodel.CarritoViewModelFactory
import androidx.activity.viewModels // Para usar viewModels()
import com.example.myapplication.adapters.CarritoAdapter

import android.widget.LinearLayout // Para el layoutCarritoVacio
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton // Importar MaterialButton

class CarritoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar // Declaración de la Toolbar
    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var textViewCarritoVacio: TextView
    private lateinit var layoutCarritoVacio: LinearLayout // Nuevo: Layout para el mensaje de carrito vacío
    private lateinit var textViewTotalCarrito: TextView
    private lateinit var textViewSubtotalItems: TextView // Nuevo: TextView para el subtotal
    private lateinit var layoutResumenCarrito: LinearLayout // Nuevo: Layout para el resumen del carrito
    private lateinit var buttonPagar: MaterialButton // Ahora es MaterialButton

    private lateinit var carritoAdapter: CarritoAdapter // Adaptador para el RecyclerView
    private val carritoViewModel: CarritoViewModel by viewModels {
        CarritoViewModelFactory(RetrofitClient.instance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        // Inicializar la Toolbar
        toolbar = findViewById(R.id.toolbarCarrito)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Botón de regreso
        supportActionBar?.title = "Mi Carrito" // Título de la toolbar

        // Inicializar vistas
        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito)
        textViewCarritoVacio = findViewById(R.id.textViewCarritoVacio)
        layoutCarritoVacio = findViewById(R.id.layoutCarritoVacio) // Inicializar el layout de vacío
        textViewTotalCarrito = findViewById(R.id.textViewTotalCarrito)
        textViewSubtotalItems = findViewById(R.id.textViewSubtotalItems) // Inicializar subtotal
        layoutResumenCarrito = findViewById(R.id.layoutResumenCarrito) // Inicializar el layout de resumen
        buttonPagar = findViewById(R.id.buttonPagar) // Ahora es MaterialButton

        // Configurar RecyclerView
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador. Se le pasa una lambda para manejar los cambios de cantidad
        carritoAdapter = CarritoAdapter(
            onQuantityChange = { producto, newQuantity ->
                // Actualiza la cantidad en CarritoManager
                CarritoManager.actualizarCantidad(producto, newQuantity)
            },
            onRemoveItem = { producto ->
                // Elimina el producto del carrito si la cantidad llega a 0
                CarritoManager.removerProductoDelCarrito(producto)
            }
        )
        recyclerViewCarrito.adapter = carritoAdapter

        // Observar los ítems del carrito desde CarritoManager
        CarritoManager.carritoItems.observe(this, Observer { items ->
            // Actualiza el adaptador con los nuevos ítems
            carritoAdapter.submitList(items) // Asumo que CarritoAdapter usa ListAdapter o similar

            // Mostrar/ocultar mensaje de carrito vacío y sección de resumen
            if (items.isEmpty()) {
                layoutCarritoVacio.visibility = View.VISIBLE
                recyclerViewCarrito.visibility = View.GONE
                layoutResumenCarrito.visibility = View.GONE
            } else {
                layoutCarritoVacio.visibility = View.GONE
                recyclerViewCarrito.visibility = View.VISIBLE
                layoutResumenCarrito.visibility = View.VISIBLE
            }
        })

        // Observar el total del carrito desde CarritoManager
        CarritoManager.totalCarrito.observe(this, Observer { total ->
            textViewTotalCarrito.text = "$${String.format("%.2f", total)}"
            // Calcular y mostrar subtotal de ítems (suma de (precio * cantidad) de cada item)
            val subtotalItems = CarritoManager.carritoItems.value?.sumOf { it.producto.precio * it.cantidad } ?: 0.0
            textViewSubtotalItems.text = "$${String.format("%.2f", subtotalItems)}"
        })

        // Listener para el botón de Pagar
        buttonPagar.setOnClickListener {
            // Llama a la función del ViewModel para procesar el pago
            carritoViewModel.realizarPago()
        }

        // Observar resultados del pago desde ViewModel
        carritoViewModel.checkoutResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Pago realizado con éxito. Carrito vaciado.", Toast.LENGTH_SHORT).show()
                // El CarritoManager ya vacía el carrito en el ViewModel
                // Si necesitas hacer algo más después de un pago exitoso, aquí es el lugar
            }
        })

        // Observar mensajes de error del ViewModel
        carritoViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    // Manejar el clic en el botón de regreso de la Toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
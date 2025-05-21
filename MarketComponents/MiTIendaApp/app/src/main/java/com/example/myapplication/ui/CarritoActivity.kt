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

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var textViewTotalCarrito: TextView
    private lateinit var buttonPagar: Button
    private lateinit var textViewCarritoVacio: TextView

    // Mantén el CarritoViewModel aquí, pero solo para la LOGICA DE PAGO (API).
    // No gestionará los ítems del carrito directamente, eso lo hará CarritoManager.
    private val carritoViewModel: CarritoViewModel by viewModels {
        CarritoViewModelFactory(RetrofitClient.instance)
    }

    private lateinit var carritoAdapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarCarrito)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mi Carrito"

        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito)
        textViewTotalCarrito = findViewById(R.id.textViewTotalCarrito)
        buttonPagar = findViewById(R.id.buttonPagar)
        textViewCarritoVacio = findViewById(R.id.textViewCarritoVacio)

        setupRecyclerView()
        observeCarritoState() // Renombramos la función para observar el estado del carrito
        setupListeners()
    }

    private fun setupRecyclerView() {
        carritoAdapter = CarritoAdapter(
            onQuantityChanged = { carritoItem, newQuantity ->
                // Llama al CarritoManager para actualizar la cantidad
                CarritoManager.actualizarCantidad(carritoItem.producto, newQuantity)
            }
        )
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)
        recyclerViewCarrito.adapter = carritoAdapter
    }

    private fun observeCarritoState() {
        // Observa el LiveData del Singleton CarritoManager para los ítems del carrito
        CarritoManager.carritoItems.observe(this, Observer { items ->
            carritoAdapter.submitList(items.toMutableList())

            if (items.isEmpty()) {
                textViewCarritoVacio.visibility = View.VISIBLE
                recyclerViewCarrito.visibility = View.GONE
                buttonPagar.isEnabled = false // Deshabilita el botón si no hay ítems
            } else {
                textViewCarritoVacio.visibility = View.GONE
                recyclerViewCarrito.visibility = View.VISIBLE
                buttonPagar.isEnabled = true // Habilita el botón si hay ítems
            }
        })

        // Observa el LiveData del Singleton CarritoManager para el total
        CarritoManager.totalCarrito.observe(this, Observer { total ->
            textViewTotalCarrito.text = String.format("$%.2f", total)
        })

        // Observa los resultados del pago del CarritoViewModel (que gestiona la API)
        carritoViewModel.checkoutResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "¡Pedido realizado con éxito!", Toast.LENGTH_LONG).show()
                // Después de un pago exitoso, el CarritoManager ya vació el carrito
                finish() // Cierra la actividad del carrito
            }
        })

        carritoViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupListeners() {
        buttonPagar.setOnClickListener {
            // Llama a la función de pago del CarritoViewModel.
            // Este ViewModel internamente accederá a los ítems del CarritoManager.
            carritoViewModel.realizarPago()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
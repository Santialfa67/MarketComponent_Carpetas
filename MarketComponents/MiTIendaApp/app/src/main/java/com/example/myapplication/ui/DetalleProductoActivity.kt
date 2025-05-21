package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.model.Producto
import com.squareup.picasso.Picasso

import androidx.activity.viewModels
import com.example.myapplication.network.RetrofitClient // Asegúrate de que esta ruta sea correcta
import com.example.myapplication.ui.DetalleProducto.DetalleProductoViewModel // Asumiendo que tu ViewModel está aquí
import com.example.myapplication.ui.viewmodel.DetalleProductoViewModelFactory // Asumiendo que tu Factory está aquí



// Importaciones para los nuevos elementos de Material Design
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton // Importar MaterialButton

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar // Declaración de la nueva Toolbar
    private lateinit var textViewNombreDetalle: TextView
    private lateinit var textViewPrecioDetalle: TextView
    private lateinit var textViewDescripcionDetalle: TextView
    private lateinit var imageViewDetalle: ImageView
    private lateinit var textViewStockDetalle: TextView
    private lateinit var textViewProveedorDetalle: TextView
    // private lateinit var botonAgregarCarrito: Button // ELIMINAR O CAMBIAR A MaterialButton
    private lateinit var botonAgregarCarrito: MaterialButton // Ahora es MaterialButton

    // Para el contador de cantidad
    private lateinit var textViewQuantity: TextView
    private lateinit var buttonMinus: MaterialButton
    private lateinit var buttonPlus: MaterialButton
    private var currentQuantity: Int = 1 // Cantidad inicial, por defecto 1

    // ViewModel para obtener detalles del producto si es necesario
    private val detalleProductoViewModel: DetalleProductoViewModel by viewModels {
        DetalleProductoViewModelFactory(RetrofitClient.instance)
    }

    private var currentProduct: Producto? = null // Para guardar el producto actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)

        // Inicializar la Toolbar (usando el nuevo ID)
        toolbar = findViewById(R.id.toolbarDetalle)
        setSupportActionBar(toolbar)
        // Habilitar el botón de regreso (flecha)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "" // Limpiar el título de la toolbar si lo manejas con un TextView en el XML de toolbar

        // Inicializa todas las vistas
        textViewNombreDetalle = findViewById(R.id.textViewNombreDetalle)
        textViewPrecioDetalle = findViewById(R.id.textViewPrecioDetalle)
        textViewDescripcionDetalle = findViewById(R.id.textViewDescripcionDetalle)
        imageViewDetalle = findViewById(R.id.imageViewDetalle)
        textViewStockDetalle = findViewById(R.id.textViewStockDetalle)
        textViewProveedorDetalle = findViewById(R.id.textViewProveedorDetalle)
        botonAgregarCarrito = findViewById(R.id.botonAgregarCarrito) // Ahora será MaterialButton

        // Inicializar vistas del contador
        textViewQuantity = findViewById(R.id.textViewQuantity)
        buttonMinus = findViewById(R.id.buttonMinus)
        buttonPlus = findViewById(R.id.buttonPlus)

        // OBTENER EL PRODUCTO: Prioriza recibirlo por Intent, sino, cargarlo por ID
        val productoRecibido: Producto? = intent.getParcelableExtra("producto")
        val productoIdRecibido: Int = intent.getIntExtra("productoId", -1)

        if (productoRecibido != null) {
            Log.d("DetalleProducto", "Producto recibido por Intent: ID=${productoRecibido.producto_id}, Nombre=${productoRecibido.nombre}")
            currentProduct = productoRecibido // Guarda el producto actual
            displayProductoDetails(productoRecibido)
        } else if (productoIdRecibido != -1) {
            Log.d("DetalleProducto", "Producto ID recibido para carga: ID=${productoIdRecibido}")
            detalleProductoViewModel.obtenerDetalleProducto(productoIdRecibido)
            observeDetalleProductoViewModel()
        } else {
            Toast.makeText(this, "Error: No se recibió información del producto.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Configurar listeners para el contador de cantidad
        textViewQuantity.text = currentQuantity.toString() // Muestra la cantidad inicial
        buttonMinus.setOnClickListener {
            if (currentQuantity > 1) { // La cantidad mínima es 1
                currentQuantity--
                textViewQuantity.text = currentQuantity.toString()
            }
        }
        buttonPlus.setOnClickListener {
            currentQuantity++
            textViewQuantity.text = currentQuantity.toString()
        }

        // Asigna el listener al botón de agregar al carrito
        // Dentro de tu DetalleProductoActivity.kt, en el setOnClickListener del botón Añadir al Carrito
        botonAgregarCarrito.setOnClickListener {
            currentProduct?.let { producto ->
                // Ahora usamos 'cantidadSeleccionada' en lugar de 'stock' para la cantidad en el carrito
                val productoParaCarrito = producto.copy(cantidadSeleccionada = currentQuantity)
                CarritoManager.agregarProductoAlCarrito(productoParaCarrito)
                Toast.makeText(this, "Añadidos $currentQuantity unidades de ${producto.nombre} al carrito", Toast.LENGTH_SHORT).show()
                Log.d("DetalleProducto", "Producto añadido al CarritoManager: ID=${producto.producto_id}, Nombre=${producto.nombre}, Cantidad=${currentQuantity}")
            } ?: Toast.makeText(this, "No se pudo añadir el producto al carrito.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeDetalleProductoViewModel() {
        detalleProductoViewModel.detalleProducto.observe(this, Observer { producto ->
            producto?.let {
                Log.d("DetalleProducto", "Producto cargado por ViewModel: ID=${it.producto_id}, Nombre=${it.nombre}")
                currentProduct = it // Guarda el producto actual
                displayProductoDetails(it)
            } ?: run {
                Toast.makeText(this, "Producto no encontrado.", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        detalleProductoViewModel.isLoading.observe(this, Observer { isLoading ->
            // Muestra/oculta un ProgressBar si lo tienes
            // Log.d("DetalleProducto", "Cargando producto: $isLoading")
        })

        detalleProductoViewModel.error.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, "Error de carga: $it", Toast.LENGTH_LONG).show()
                Log.e("DetalleProducto", "Error de carga: $it")
            }
        })
    }

    private fun displayProductoDetails(producto: Producto) {
        toolbar.title = producto.nombre // Establece el título de la Toolbar
        textViewNombreDetalle.text = producto.nombre
        textViewPrecioDetalle.text = "$${String.format("%.2f", producto.precio)}"
        textViewDescripcionDetalle.text = producto.descripcion

        producto.imagen?.let { imageUrl ->
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground) // Asegúrate de tener este drawable
                .error(R.drawable.ic_launcher_background) // Asegúrate de tener este drawable
                .into(imageViewDetalle)
        } ?: run {
            imageViewDetalle.setImageResource(R.drawable.ic_launcher_background)
        }

        textViewStockDetalle.text = "Stock: ${producto.stock ?: "No disponible"}"
        textViewProveedorDetalle.text = "Proveedor: ${producto.proveedor?.nombre ?: "No disponible"}"

        // Inicializa la cantidad a 1 y actualiza la UI del contador
        currentQuantity = 1
        textViewQuantity.text = currentQuantity.toString()
    }

    // Manejar el clic en el botón de regreso de la Toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
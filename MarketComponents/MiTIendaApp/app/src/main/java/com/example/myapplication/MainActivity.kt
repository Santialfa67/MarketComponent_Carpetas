package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Categoria
import com.example.myapplication.model.Producto
import com.example.myapplication.ui.categoria.CategoriaAdapter
import com.example.myapplication.ui.categoria.CategoriaViewModel


import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer // Importar Observer explícitamente
import androidx.recyclerview.widget.GridLayoutManager // Importar GridLayoutManager


class MainActivity : AppCompatActivity(), ProductoAdapter.OnItemClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerViewProductos: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private var listaDeProductos: List<Producto> = emptyList()
    private val carritoDeCompras = mutableListOf<Producto>() // Carrito de compras local
    private lateinit var startDetalleActivityForResult: ActivityResultLauncher<Intent>
    private var carritoMenuItem: MenuItem? = null
    // Se elimina: private lateinit var imageViewCarritoToolbar: ImageView // ESTA LÍNEA SE ELIMINA

    private val categoriaViewModel: CategoriaViewModel by viewModels()
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var recyclerViewCategorias: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        // Inicializar vistas
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias)
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos)
        // Se elimina: imageViewCarritoToolbar = findViewById(R.id.imageViewCarritoToolbar) // ESTA LÍNEA SE ELIMINA

        setupRecyclerViews()
        setupActivityResultLauncher()
        // Se elimina: setupToolbarListeners() // ESTA FUNCIÓN YA NO ES NECESARIA
        observeViewModels()
    }

    private fun setupRecyclerViews() {
        recyclerViewCategorias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoriaAdapter = CategoriaAdapter(emptyList()) { categoria ->
            val categoriaId = categoria.categoria_id
            Log.d("MainActivity", "Categoría clickeada en el Adapter: ${categoria.nombre}, ID para productos: $categoriaId")
            categoriaViewModel.obtenerProductosPorCategoria(categoriaId.toInt())
        }
        recyclerViewCategorias.adapter = categoriaAdapter

        recyclerViewProductos.layoutManager = GridLayoutManager(this, 2)
        productoAdapter = ProductoAdapter(emptyList(), this)
        recyclerViewProductos.adapter = productoAdapter
    }

    private fun setupActivityResultLauncher() {
        startDetalleActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val productoAñadido = result.data?.getParcelableExtra<Producto>("producto_añadido")
                productoAñadido?.let {
                    carritoDeCompras.add(it) // Añade el producto al carrito local
                    // Ya no necesitas actualizar el contador visual si lo eliminaste
                    // actualizarContadorCarrito() // Si NO tienes badge de contador, comenta o elimina esto
                    Toast.makeText(this, "${it.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Se elimina esta función por completo
    // private fun setupToolbarListeners() {
    //     imageViewCarritoToolbar.setOnClickListener {
    //         val intent = Intent(this, CarritoActivity::class.java)
    //         intent.putParcelableArrayListExtra("carrito", ArrayList(carritoDeCompras))
    //         startActivity(intent)
    //     }
    // }

    private fun observeViewModels() {
        categoriaViewModel.categorias.observe(this, Observer { categorias ->
            categorias?.let {
                categoriaAdapter.actualizarCategorias(it)
            }
            categoriaViewModel.obtenerTodosLosProductos()
        })

        categoriaViewModel.productosDeCategoria.observe(this, Observer { productos ->
            Log.d("MainActivity", "Lista de productos observada, tamaño: ${productos.size}")
            productos.forEach {
                Log.d("MainActivity", "Producto: ${it.nombre}, Imagen URL: ${it.imagen}")
            }
            listaDeProductos = productos
            productoAdapter.actualizarProductos(productos)
        })

        categoriaViewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error: $error")
        })

        categoriaViewModel.isLoadingCategorias.observe(this, Observer { isLoading ->
            Log.d("MainActivity", "Cargando categorías: $isLoading")
        })

        categoriaViewModel.isLoadingProductos.observe(this, Observer { isLoading ->
            Log.d("MainActivity", "Cargando productos: $isLoading")
        })

        categoriaViewModel.obtenerCategorias()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        carritoMenuItem = menu?.findItem(R.id.action_carrito)

        // Si previamente tenías un actionLayout para el carrito y un TextView para el badge,
        // y decidiste eliminar el badge, esta sección también debe ser comentada/eliminada.
        // Si solo quieres el icono simple del carrito del menu_main.xml, no necesitas esto.
        // val cartActionView = carritoMenuItem?.actionView
        // cartActionView?.setOnClickListener {
        //     onOptionsItemSelected(carritoMenuItem!!)
        // }

        // Si eliminaste el badge del carrito, esta llamada ya no es necesaria
        // O si CarritoManager es el que gestiona el badge, la lógica iría allí
        // actualizarContadorCarrito() // Comenta o elimina si ya no tienes un contador visual en el menú
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_carrito -> {
                val intent = Intent(this, CarritoActivity::class.java)
                // Pasa los ítems del carrito de compras local a la CarritoActivity
                intent.putParcelableArrayListExtra("carrito", ArrayList(carritoDeCompras))
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(producto: Producto) {
        val intent = Intent(this, DetalleProductoActivity::class.java)
        intent.putExtra("producto", producto)
        startDetalleActivityForResult.launch(intent)
    }

    // Esta función ya no es necesaria si eliminaste el badge de contador visual
    // Si la mantienes, asegúrate de que R.id.cart_badge exista en menu_item_cart_layout.xml
    // (si es que aún usas un actionLayout para el carrito)
    // private fun actualizarContadorCarrito() {
    //     val cartBadgeTextView = carritoMenuItem?.actionView?.findViewById<TextView>(R.id.cart_badge)
    //     val itemCount = carritoDeCompras.size // Usa el tamaño del carrito local
    //     cartBadgeTextView?.text = itemCount.toString()
    //     cartBadgeTextView?.visibility = if (itemCount > 0) View.VISIBLE else View.GONE
    // }
}

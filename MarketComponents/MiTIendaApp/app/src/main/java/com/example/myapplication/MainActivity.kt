package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button // Importar Button
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.ui.LoginActivity
import com.example.myapplication.utils.SessionManager


class MainActivity : AppCompatActivity(), ProductoAdapter.OnItemClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerViewProductos: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private var listaDeProductos: List<Producto> = emptyList()
    private val carritoDeCompras = mutableListOf<Producto>()
    private lateinit var startDetalleActivityForResult: ActivityResultLauncher<Intent>
    private var carritoMenuItem: MenuItem? = null

    private val categoriaViewModel: CategoriaViewModel by viewModels()
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var recyclerViewCategorias: RecyclerView
    private lateinit var buttonLogout: Button // Declarar el botón de logout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        // Inicializar vistas
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias)
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos)
        buttonLogout = findViewById(R.id.buttonLogout) // Inicializar el botón de logout

        setupRecyclerViews()
        setupActivityResultLauncher()
        observeViewModels()

        // Lógica para el botón de cerrar sesión
        buttonLogout.setOnClickListener {
            performLogout() // Llama a la función de logout
        }

        // --- ELIMINAR ESTE BLOQUE DE CÓDIGO DE AQUÍ ---
        // if (sessionManager.isLoggedIn()) {
        //     val intent = Intent(this, MainActivity::class.java)
        //     startActivity(intent)
        // } else {
        //     val intent = Intent(this, LoginActivity::class.java)
        //     startActivity(intent)
        // }
        // finish()
        // ---------------------------------------------
        // Esta lógica debe estar en la primera actividad que se lanza (ej. SplashActivity)
        // para decidir a dónde ir después de iniciar la app.
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
                    carritoDeCompras.add(it)
                    Toast.makeText(this, "${it.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_carrito -> {
                val intent = Intent(this, CarritoActivity::class.java)
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

    // Función para cerrar sesión
    private fun performLogout() {
        val sessionManager = SessionManager(this)
        sessionManager.logout() // Borra los datos de sesión

        // Redirige a la pantalla de Login y borra el historial de actividades
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cierra MainActivity
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
    }

    // Opcional: Controlar el botón de retroceso para que no vuelva al login si ya se deslogueó
    override fun onBackPressed() {
        val sessionManager = SessionManager(this)
        if (!sessionManager.isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed() // Comportamiento normal si aún está logueado
        }
    }
}
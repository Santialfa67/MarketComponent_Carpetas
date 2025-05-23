package com.example.myapplication.network

import com.example.myapplication.data.request.PedidoRequest
import com.example.myapplication.model.AuthResponse
import com.example.myapplication.model.Producto
import com.example.myapplication.model.Categoria
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response // Para obtener el cuerpo de la respuesta o manejar 200 OK


//interface ApiService {
//    @GET("/api/categorias")
//    suspend fun obtenerCategorias(
//        @Header("apikey") apiKey: String,
//        @Header("Content-Type") contentType: String = "application/json",
//        @Header("Accept") accept: String = "application/vnd.pgrst.object+json" // O application/json si esperas un array
//    ): List<Categoria>
//}
//interface ApiService {
//    @GET("/rest/v1/categorias")
//    suspend fun obtenerCategorias(
//        @Header("apikey") apiKey: String,
//        @Header("Authorization") authorization: String,
//        @Header("Accept") accept: String = "application/json"
//    ): List<Categoria>
//}

//interface ApiService {
//    @GET("api/categorias") // Endpoint correcto
//    suspend fun obtenerCategorias(): List<Categoria>
//}

interface ApiService {

    @GET("api/productos") // ¡Asegúrate de tener este endpoint para obtener todos los productos!
    suspend fun getProductos(): Response<List<Producto>>

    @GET("api/categorias")
    suspend fun obtenerCategorias(
        //@Header("Content-Type") contentType: String = "application/json",
        //@Header("Accept") accept: String = "application/vnd.pgrst.object+json"
    ): List<Categoria>

    @GET("api/productos/categoria/{categoriaId}")
    suspend fun obtenerProductosPorCategoria(
        @Path("categoriaId") categoriaId: Int
    ): List<Producto>


    @GET("api/productos/{id}")
    suspend fun obtenerDetalleProducto(
        @Path("id") id: Int
    ): Producto

    @POST("api/pedidos/procesar")
    suspend fun procesarPedido(@Body pedidoRequest: PedidoRequest): Response<Void>

    @POST("api/usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Void>


//    @POST("api/auth/login") // Ajusta la ruta a tu endpoint de login en el backend
//    suspend fun loginUsuario(@Body request: LoginRequest): Response<String> // O Rsponse<LoginResponse> si tu backend devuelve un objeto

    @POST("api/auth/login") // Ajusta la ruta a tu endpoint de login en el backend
    suspend fun loginUsuario(@Body request: LoginRequest): Response<AuthResponse>
}
package com.example.myapplication.ui

import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.Usuario
import com.example.myapplication.network.RetrofitClient // Asegúrate de que este es el ApiClient correcto
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import android.content.Intent
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {

    // Declarar el TextView para el enlace al login
    private lateinit var textViewAlreadyAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inputs
        val nameInput = findViewById<EditText>(R.id.register_name)
        val emailInput = findViewById<EditText>(R.id.register_email)
        val passwordInput = findViewById<EditText>(R.id.register_password)
        val phoneInput = findViewById<EditText>(R.id.register_phone)
        val addressInput = findViewById<EditText>(R.id.register_address)

        // Botón para registrar
        val registerButton = findViewById<MaterialButton>(R.id.btnCreateAccount)

        // Inicializar el TextView del enlace al login
        textViewAlreadyAccount = findViewById(R.id.textViewAlreadyAccount) // Asegúrate que el ID coincida con tu XML

        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val address = addressInput.text.toString().trim()

            if (!validarCampos(name, email, password, phone, address)) return@setOnClickListener

            // Asegúrate de que el constructor de Usuario acepte null para fechaRegistro y preferencias
            // O pasa valores por defecto si no son nulos en el backend
            val usuario = Usuario(
                userId = null,
                nombre = name,
                email = email,
                password = password,
                telefono = phone,
                direccion = address,

            )

            registrarUsuario(usuario)
        }

        // Configurar el OnClickListener para el TextView "Ya tienes cuenta? Inicia sesión"
        textViewAlreadyAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Cierra RegisterActivity para que no se acumule en la pila
        }
    }

    private fun validarCampos(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!phone.all { it.isDigit() } || phone.length < 7) {
            Toast.makeText(this, "Teléfono inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registrarUsuario(usuario: Usuario) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Aquí usamos RetrofitClient.instance, asumo que es tu ApiService configurado
                val response = RetrofitClient.instance.registrarUsuario(usuario)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // Vuelve a la actividad anterior (probablemente LoginActivity)
                    } else {
                        // Mejor manejo de errores: Si el backend devuelve un JSON de error,
                        // intenta parsearlo. Si no, muestra el código de error.
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = if (errorBody != null && errorBody.isNotEmpty()) {
                            // Si tu backend devuelve ErrorResponse en caso de error, puedes parsearlo aquí
                            // Usar Gson().fromJson(errorBody, ErrorResponse::class.java).message
                            "Error: $errorBody" // Por ahora, muestra el cuerpo crudo
                        } else {
                            "Error al registrar: ${response.code()}"
                        }
                        Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    // Similar al bloque isSuccessful, intenta parsear el error HTTP
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = if (errorBody != null && errorBody.isNotEmpty()) {
                        // Puedes usar Gson().fromJson(errorBody, ErrorResponse::class.java).message
                        "Error HTTP: ${e.code()} - $errorBody"
                    } else {
                        "Error HTTP: ${e.code()} - ${e.message}"
                    }
                    Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) { // Capturar cualquier otra excepción inesperada
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
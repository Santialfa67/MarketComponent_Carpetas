package com.example.myapplication.ui

import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.Usuario
import com.example.myapplication.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

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

        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val address = addressInput.text.toString().trim()

            if (!validarCampos(name, email, password, phone, address)) return@setOnClickListener

            val usuario = Usuario(
                nombre = name,
                email = email,
                password = password,
                telefono = phone,
                direccion = address
            )

            registrarUsuario(usuario)
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
                val response = RetrofitClient.instance.registrarUsuario(usuario)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // volver al login
                    } else {
                        Toast.makeText(this@RegisterActivity, "Error al registrar: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error HTTP: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
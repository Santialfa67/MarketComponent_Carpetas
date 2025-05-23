package com.example.myapplication.ui

import android.os.Bundle
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
            val phone = phoneInput.text.toString().trim()
            val address = addressInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() &&
                email.isNotEmpty() && password.isNotEmpty()) {

                val usuario = Usuario(
                    nombre = name,
                    email = email,
                    password = password,
                    telefono = phone,
                    direccion = address
                )

                registrarUsuario(usuario)

            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
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
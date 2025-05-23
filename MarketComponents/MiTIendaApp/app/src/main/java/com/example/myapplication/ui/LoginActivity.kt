package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.AuthResponse // Importa AuthResponse
import com.example.myapplication.model.ErrorResponse // Importa ErrorResponse
import com.example.myapplication.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson // Para parsear el cuerpo de error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var textviewRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.editTextEmail)
        passwordInput = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        textviewRegister = findViewById(R.id.textViewRegister)

        textviewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                performLogin(email, password)
            } else {
                Toast.makeText(this, "Por favor, introduce tu email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = RetrofitClient.instance.loginUsuario(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val authResponse = response.body()
                        Toast.makeText(this@LoginActivity, "¡Login exitoso! ${authResponse?.message}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBodyString = response.errorBody()?.string()
                        val errorMessage = try {
                            if (!errorBodyString.isNullOrEmpty()) {
                                val errorResponse = Gson().fromJson(errorBodyString, ErrorResponse::class.java)
                                errorResponse.message // O cualquier otro campo que quieras mostrar
                            } else {
                                "Error desconocido"
                            }
                        } catch (e: Exception) {
                            "Error al procesar la respuesta de error: $errorBodyString"
                        }
                        Toast.makeText(this@LoginActivity, "Error en el login: ${response.code()} - $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    val errorBodyString = e.response()?.errorBody()?.string()
                    val errorMessage = try {
                        if (!errorBodyString.isNullOrEmpty()) {
                            val errorResponse = Gson().fromJson(errorBodyString, ErrorResponse::class.java)
                            errorResponse.message
                        } else {
                            "Error desconocido"
                        }
                    } catch (ex: Exception) {
                        "Error al procesar la respuesta HTTP: $errorBodyString"
                    }
                    Toast.makeText(this@LoginActivity, "Error HTTP: ${e.code()} - $errorMessage", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
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
import com.example.myapplication.model.ErrorResponseU // Importa ErrorResponse
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.utils.SessionManager
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
                        val authResponse = response.body() // Esto es un objeto AuthResponse
                        val sessionManager = SessionManager(this@LoginActivity)

                        // **AJUSTE CRUCIAL:**
                        // Asegúrate de que tu `AuthResponse` tenga estos campos.
                        // Si tu backend no los envía en la respuesta de login,
                        // DEBES modificar tu backend para que los incluya.
                        // Si un campo es opcional (puede ser null), asegúrate de que tu AuthResponse
                        // lo declare como nullable (ej. `val userPhone: String?`).

                        // Se usa el operador Elvis (?:) para proporcionar un valor por defecto
                        // si el campo de authResponse es null.
                        val userId = authResponse?.userId ?: -1 // Si userId es Int?, toma el valor o -1 si es null
                        val userName = authResponse?.userName // Si userName es String?, toma el valor o null si es null
                        val userPhone = authResponse?.userPhone // Si userPhone es String?, toma el valor o null si es null
                        val userAddress = authResponse?.userAddress // Si userAddress es String?, toma el valor o null si es null


                        sessionManager.saveLoginState(
                            isLoggedIn = true,
                            token = authResponse?.token, // Token podría ser null si no hay respuesta, aunque improbable en isSuccessful
                            email = authResponse?.email, // Email podría ser null
                            userId = userId, // userId ahora es un Int (o Int? si así lo definiste en SessionManager)
                            userName = userName,
                            userPhone = userPhone,
                            userAddress = userAddress
                        )

                        Toast.makeText(this@LoginActivity, "¡Login exitoso!", Toast.LENGTH_SHORT).show() // No es necesario el mensaje del backend aquí
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val errorBodyString = response.errorBody()?.string()
                        val errorMessage = try {
                            if (!errorBodyString.isNullOrEmpty()) {
                                // Aquí se asume que ErrorResponse tiene un campo 'message'
                                val errorResponse = Gson().fromJson(errorBodyString, ErrorResponseU::class.java)
                                errorResponse.message
                            } else {
                                "Error desconocido del servidor"
                            }
                        } catch (e: Exception) {
                            "Error al procesar la respuesta de error: $errorBodyString (formato JSON inválido)"
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
                            // Aquí se asume que ErrorResponse tiene un campo 'message'
                            val errorResponse = Gson().fromJson(errorBodyString, ErrorResponseU::class.java)
                            errorResponse.message
                        } else {
                            "Error desconocido del servidor"
                        }
                    } catch (ex: Exception) {
                        "Error al procesar la respuesta HTTP: ${e.code()} - $errorBodyString (formato JSON inválido)"
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
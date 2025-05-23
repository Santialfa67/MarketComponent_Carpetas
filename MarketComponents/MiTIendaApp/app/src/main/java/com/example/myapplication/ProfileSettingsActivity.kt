package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.model.Usuario
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.utils.SessionManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProfileSettingsActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var addressInput: EditText // Se mantiene si se sigue usando para editar perfil
    private lateinit var buttonSaveChanges: MaterialButton
    private lateinit var buttonChangePassword: MaterialButton

    private lateinit var sessionManager: SessionManager
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        sessionManager = SessionManager(this)

        // Inicializar vistas
        nameInput = findViewById(R.id.profile_name)
        emailInput = findViewById(R.id.profile_email)
        phoneInput = findViewById(R.id.profile_phone)
        addressInput = findViewById(R.id.profile_address) // Se mantiene
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges)
        buttonChangePassword = findViewById(R.id.buttonChangePassword)

        loadUserProfile()

        buttonSaveChanges.setOnClickListener {
            saveProfileChanges()
        }

        buttonChangePassword.setOnClickListener {
            Toast.makeText(this, "Funcionalidad de cambiar contraseña (próximamente)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserProfile() {
        nameInput.setText(sessionManager.getUserName())
        emailInput.setText(sessionManager.getUserEmail())
        phoneInput.setText(sessionManager.getUserPhone())
        addressInput.setText(sessionManager.getUserAddress()) // Se mantiene
        currentUserId = sessionManager.getUserId()

        if (currentUserId == -1) {
            Toast.makeText(
                this,
                "ID de usuario no encontrado. Vuelve a iniciar sesión.",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun saveProfileChanges(): Boolean {
        val updatedName = nameInput.text.toString().trim()
        val updatedPhone = phoneInput.text.toString().trim()
        val updatedAddress = addressInput.text.toString().trim() // Se mantiene

        if (updatedName.isEmpty() || updatedPhone.isEmpty() || updatedAddress.isEmpty()) { // Validar también la dirección
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!updatedPhone.all { it.isDigit() } || updatedPhone.length < 7) {
            Toast.makeText(this, "Teléfono inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (currentUserId == -1) {
            Toast.makeText(this, "No se pudo obtener el ID del usuario. Intenta reiniciar la aplicación.", Toast.LENGTH_LONG).show()
            return false
        }

        // Crear un objeto Usuario con los datos actualizados
        // Solo incluye los campos que necesitas y que tu backend espera para una actualización.
        val updatedUser = Usuario(
            userId = currentUserId,
            nombre = updatedName,
            email = emailInput.text.toString(), // El email generalmente no se cambia en esta pantalla
            password = "", // Se envía vacío si no se cambia la contraseña aquí
            telefono = updatedPhone,
            direccion = updatedAddress // Se mantiene si se usa en el perfil
            // No incluir fecha_registro ni preferencias
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.actualizarUsuario(currentUserId, updatedUser)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProfileSettingsActivity, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        sessionManager.saveLoginState(
                            isLoggedIn = true,
                            token = sessionManager.getAuthToken(),
                            email = sessionManager.getUserEmail(),
                            userId = currentUserId,
                            userName = updatedName,
                            userPhone = updatedPhone,
                            userAddress = updatedAddress // Se actualiza en SessionManager
                        )
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = if (errorBody != null && errorBody.isNotEmpty()) {
                            "Error al actualizar: $errorBody"
                        } else {
                            "Error al actualizar: ${response.code()}"
                        }
                        Toast.makeText(this@ProfileSettingsActivity, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("ProfileSettings", "Error al actualizar perfil: ${response.code()} - $errorBody")
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProfileSettingsActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("ProfileSettings", "Error de red", e)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = if (errorBody != null && errorBody.isNotEmpty()) {
                        "Error HTTP: ${e.code()} - $errorBody"
                    } else {
                        "Error HTTP: ${e.code()} - ${e.message}"
                    }
                    Toast.makeText(this@ProfileSettingsActivity, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("ProfileSettings", "Error HTTP", e)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProfileSettingsActivity, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("ProfileSettings", "Error inesperado", e)
                }
            }
        }
        return true
    }
}
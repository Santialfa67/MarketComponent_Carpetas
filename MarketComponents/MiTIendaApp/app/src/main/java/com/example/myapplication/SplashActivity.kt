// com.example.myapplication/SplashActivity.kt (nuevo archivo)
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.LoginActivity
import com.example.myapplication.utils.SessionManager // Asegúrate de que la ruta sea correcta

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No necesitas un layout complejo para una SplashActivity si solo rediriges
        // Puedes poner setContentView(R.layout.activity_splash) si tienes un diseño de splash
        // o simplemente dejarlo así si quieres una pantalla negra rápida.

        val sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            // Usuario ya logeado, ir a la pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // Usuario no logeado, ir a la pantalla de login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish() // Cierra la SplashActivity para que el usuario no pueda volver a ella
    }
}
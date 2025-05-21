package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Update these lines to match your XML IDs
        val usernameInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)  // Changed from ImageButton to Button
        val registerButton = findViewById<TextView>(R.id.buttonRegister)

        val intent = Intent(this, MainActivity::class.java)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username == "a" && password == "a") {
                Toast.makeText(this, "Acceso correcto", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            Toast.makeText(this, "Redirigir a pantalla de registro", Toast.LENGTH_SHORT).show()

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
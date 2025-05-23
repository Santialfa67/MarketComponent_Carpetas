package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val PREF_NAME = "MyAppPrefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val KEY_AUTH_TOKEN = "authToken"
    private val KEY_USER_EMAIL = "userEmail"
    private val KEY_USER_ID = "userId"
    private val KEY_USER_NAME = "userName"
    private val KEY_USER_PHONE = "userPhone"
    private val KEY_USER_ADDRESS = "userAddress"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    // Método actualizado para guardar más datos del usuario
    fun saveLoginState(
        isLoggedIn: Boolean,
        token: String? = null,
        email: String? = null,
        userId: Int? = null, // NUEVO
        userName: String? = null, // NUEVO
        userPhone: String? = null, // NUEVO
        userAddress: String? = null // NUEVO
    ) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        token?.let { editor.putString(KEY_AUTH_TOKEN, it) }
        email?.let { editor.putString(KEY_USER_EMAIL, it) }
        if (userId != null) {
            editor.putInt(KEY_USER_ID, userId)
        } else {
            editor.remove(KEY_USER_ID) // Si es null, asegúrate de que no quede un valor viejo
        }
        userName?.let { editor.putString(KEY_USER_NAME, it) } // Guardar nombre
        userPhone?.let { editor.putString(KEY_USER_PHONE, it) } // Guardar teléfono
        userAddress?.let { editor.putString(KEY_USER_ADDRESS, it) } // Guardar dirección
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun getUserId(): Int { // Obtener ID de usuario
        return prefs.getInt(KEY_USER_ID, -1) // -1 si no se encuentra
    }

    fun getUserName(): String? { // Obtener nombre de usuario
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun getUserPhone(): String? { // Obtener teléfono
        return prefs.getString(KEY_USER_PHONE, null)
    }

    fun getUserAddress(): String? { // Obtener dirección
        return prefs.getString(KEY_USER_ADDRESS, null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
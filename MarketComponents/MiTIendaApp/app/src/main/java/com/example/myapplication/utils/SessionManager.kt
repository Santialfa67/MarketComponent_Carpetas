package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val PREF_NAME = "MyAppPrefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val KEY_AUTH_TOKEN = "authToken" // Si decides guardar un token
    private val KEY_USER_EMAIL = "userEmail" // Si quieres guardar el email del usuario logeado

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun saveLoginState(isLoggedIn: Boolean, token: String? = null, email: String? = null) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        token?.let { editor.putString(KEY_AUTH_TOKEN, it) }
        email?.let { editor.putString(KEY_USER_EMAIL, it) }
        editor.apply() // Aplica los cambios asincrónicamente
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

    fun logout() {
        editor.clear() // Borra todas las preferencias de este archivo
        editor.apply()
    }
}
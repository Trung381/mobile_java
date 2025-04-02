package com.example.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val PREF_NAME = "user_prefs"
    private val KEY_USERNAME = "username"
    private val KEY_PASSWORD = "password"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUser(username: String, password: String) {
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME, username)
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    fun getUser(): Pair<String?, String?> {
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        val password = sharedPreferences.getString(KEY_PASSWORD, null)
        return Pair(username, password)
    }

    fun deleteUser() {
        sharedPreferences.edit().clear().apply()
    }
}
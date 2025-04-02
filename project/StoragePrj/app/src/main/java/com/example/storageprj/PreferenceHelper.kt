package com.example.storageprj

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)

    fun saveUser(name: String, password: String) {
        sharedPreferences.edit().apply {
            putString("NAME", name)
            putString("PASSWORD", password)
            apply()
        }
    }

    fun deleteUser() {
        sharedPreferences.edit().clear().apply()
    }

    fun getUser(): Pair<String?, String?> {
        return Pair(sharedPreferences.getString("NAME", null), sharedPreferences.getString("PASSWORD", null))
    }
}
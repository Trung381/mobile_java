package com.example.storageprj

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_preference)

        val nameInput = findViewById<EditText>(R.id.etName)
        val passwordInput = findViewById<EditText>(R.id.etPassword)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnShow = findViewById<Button>(R.id.btnShow)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val preferenceHelper = PreferenceHelper(this)

        btnSave.setOnClickListener {
            preferenceHelper.saveUser(nameInput.text.toString(), passwordInput.text.toString())
        }

        btnDelete.setOnClickListener {
            preferenceHelper.deleteUser()
        }

        btnShow.setOnClickListener {
            val (name, password) = preferenceHelper.getUser()
            tvResult.text = "Name: $name\nPassword: $password"
        }
    }
}
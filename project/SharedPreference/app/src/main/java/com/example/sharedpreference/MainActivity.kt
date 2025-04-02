package com.example.sharedpreference

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private lateinit var btnShow: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceHelper = PreferenceHelper(this)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvResult = findViewById(R.id.tvResult)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)
        btnShow = findViewById(R.id.btnShow)

        btnSave.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                preferenceHelper.saveUser(username, password)
                Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                etUsername.text.clear()
                etPassword.text.clear()
            } else {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            preferenceHelper.deleteUser()
            tvResult.text = ""
            Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show()
        }

        btnShow.setOnClickListener {
            val (username, password) = preferenceHelper.getUser()
            if (username != null && password != null) {
                tvResult.text = "Username: $username\nPassword: $password"
            } else {
                tvResult.text = "No data found"
            }
        }
    }
}
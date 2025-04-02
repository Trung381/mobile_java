package com.example.storageprj

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SQLiteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)

        val nameInput = findViewById<EditText>(R.id.etName)
        val phoneInput = findViewById<EditText>(R.id.etPhone)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnShow = findViewById<Button>(R.id.btnShow)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val dbHelper = DatabaseHelper(this)

        btnAdd.setOnClickListener {
            dbHelper.insertContact(nameInput.text.toString(), phoneInput.text.toString())
        }

        btnUpdate.setOnClickListener {
            dbHelper.updateContact(nameInput.text.toString(), phoneInput.text.toString())
        }

        btnDelete.setOnClickListener {
            dbHelper.deleteContact(nameInput.text.toString())
        }

        btnShow.setOnClickListener {
            tvResult.text = dbHelper.getContacts()
        }
    }
}
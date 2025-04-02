package com.example.sqllite

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnAdd: Button
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var btnDisplay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        tvResult = findViewById(R.id.tvResult)
        btnAdd = findViewById(R.id.btnAdd)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        btnDisplay = findViewById(R.id.btnDisplay)

        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val success = databaseHelper.addContact(name, phone)
                if (success) {
                    Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etPhone.text.clear()
                } else {
                    Toast.makeText(this, "Failed to Add Contact", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnEdit.setOnClickListener {
            val name = etName.text.toString().trim()
            val newPhone = etPhone.text.toString().trim()

            if (name.isNotEmpty() && newPhone.isNotEmpty()) {
                val success = databaseHelper.updateContact(name, newPhone)
                if (success) {
                    Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etPhone.text.clear()
                } else {
                    Toast.makeText(this, "Contact Not Found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val name = etName.text.toString().trim()

            if (name.isNotEmpty()) {
                val success = databaseHelper.deleteContact(name)
                if (success) {
                    Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etPhone.text.clear()
                } else {
                    Toast.makeText(this, "Contact Not Found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        btnDisplay.setOnClickListener {
            tvResult.text = databaseHelper.getAllContacts()
        }
    }
}

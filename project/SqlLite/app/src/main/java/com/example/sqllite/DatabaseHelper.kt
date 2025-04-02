package com.example.sqllite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "ContactsDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS contacts")
        onCreate(db)
    }

    fun addContact(name: String, phone: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("phone", phone)
        val result = db.insert("contacts", null, values)
        db.close()
        return result != -1L
    }

    fun updateContact(name: String, newPhone: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("phone", newPhone)
        val result = db.update("contacts", values, "name=?", arrayOf(name))
        db.close()
        return result > 0
    }

    fun deleteContact(name: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete("contacts", "name=?", arrayOf(name))
        db.close()
        return result > 0
    }

    fun getAllContacts(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM contacts", null)
        val stringBuilder = StringBuilder()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val phone = cursor.getString(2)
            stringBuilder.append("$id. $name - $phone\n")
        }
        cursor.close()
        db.close()
        return if (stringBuilder.isNotEmpty()) stringBuilder.toString() else "No contacts found."
    }
}

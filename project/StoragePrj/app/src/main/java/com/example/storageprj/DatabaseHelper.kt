package com.example.storageprj

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Contacts.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Contacts(Name TEXT, Phone TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Contacts")
        onCreate(db)
    }

    fun insertContact(name: String, phone: String) {
        writableDatabase.execSQL("INSERT INTO Contacts VALUES('$name', '$phone')")
    }

    fun updateContact(name: String, phone: String) {
        writableDatabase.execSQL("UPDATE Contacts SET Phone='$phone' WHERE Name='$name'")
    }

    fun deleteContact(name: String) {
        writableDatabase.execSQL("DELETE FROM Contacts WHERE Name='$name'")
    }

    fun getContacts(): String {
        val cursor = readableDatabase.rawQuery("SELECT * FROM Contacts", null)
        var result = ""
        while (cursor.moveToNext()) {
            result += "Name: ${cursor.getString(0)}, Phone: ${cursor.getString(1)}\n"
        }
        cursor.close()
        return result
    }
}
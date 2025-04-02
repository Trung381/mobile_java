package com.example.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NoteViewModel : ViewModel() {
    private val database = Firebase.database.reference.child("notes")
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    init {
        // Lắng nghe thay đổi từ Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val noteList = mutableListOf<Note>()
                for (data in snapshot.children) {
                    val note = data.getValue(Note::class.java)
                    note?.let { noteList.add(it) }
                }
                _notes.value = noteList
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }

    fun addNote(content: String) {
        val id = database.push().key ?: return
        val note = Note(id, content)
        database.child(id).setValue(note)
    }
}
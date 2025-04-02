package com.example.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.note.ui.theme.NoteTheme

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextNote = findViewById<EditText>(R.id.editTextNote)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotes)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Quan sát LiveData để cập nhật UI
        viewModel.notes.observe(this) { notes ->
            recyclerView.adapter = NoteAdapter(notes)
        }

        // Thêm ghi chú khi nhấn nút
        buttonAdd.setOnClickListener {
            val content = editTextNote.text.toString()
            if (content.isNotEmpty()) {
                viewModel.addNote(content)
                editTextNote.text.clear()
            }
        }
    }
}
package com.example.firebasetest2

import Note
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var noteAdapter: NoteAdapter
    private var notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvNotes = findViewById<RecyclerView>(R.id.rvNotes)
        val fabAddNote = findViewById<FloatingActionButton>(R.id.fabAddNote)
        val btnAddNote = findViewById<Button>(R.id.btnAddNote) // Đúng kiểu Button

        db = FirebaseFirestore.getInstance()
        noteAdapter = NoteAdapter(notes) { note -> openEditNoteActivity(note) }

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = noteAdapter

        fabAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        btnAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        loadNotes()
    }

    private fun loadNotes() {
        db.collection("notes").get().addOnSuccessListener { result ->
            notes.clear()
            for (document in result) {
                val note = document.toObject(Note::class.java).apply { id = document.id }
                notes.add(note)
            }
            noteAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load notes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openEditNoteActivity(note: Note) {
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra("note_id", note.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadNotes() // Refresh notes when coming back
    }
}

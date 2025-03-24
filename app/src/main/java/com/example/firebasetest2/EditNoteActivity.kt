package com.example.firebasetest2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditNoteActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private var noteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_mode)

        db = FirebaseFirestore.getInstance()
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Đóng activity và quay lại màn hình trước đó
        }

        noteId = intent.getStringExtra("note_id")
        if (noteId != null) {
            loadNoteDetails(noteId!!)
        }

        btnUpdate.setOnClickListener {
            updateNote()
        }

        btnDelete.setOnClickListener {
            deleteNote()
        }
    }

    private fun loadNoteDetails(noteId: String) {
        db.collection("notes").document(noteId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                etTitle.setText(document.getString("title"))
                etDescription.setText(document.getString("description"))
            }
        }
    }

    private fun updateNote() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()

        if (noteId != null) {
            db.collection("notes").document(noteId!!)
                .update(mapOf("title" to title, "description" to description))
                .addOnSuccessListener {
                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }

    private fun deleteNote() {
        if (noteId != null) {
            db.collection("notes").document(noteId!!)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}

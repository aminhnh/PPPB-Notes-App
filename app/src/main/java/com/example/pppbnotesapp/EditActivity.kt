package com.example.pppbnotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pppbnotesapp.database.Note
import com.example.pppbnotesapp.database.NoteDao
import com.example.pppbnotesapp.database.NoteRoomDatabase
import com.example.pppbnotesapp.databinding.ActivityEditBinding
import com.example.pppbnotesapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityEditBinding.inflate(layoutInflater)
    }
    private lateinit var mNotesDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        val noteId = intent.getIntExtra("id", -1)

        with(binding) {
            if (noteId != -1) {

                mNotesDao.getNoteById(noteId).observe(this@EditActivity) { note ->
                    if (note != null) {
                        with(binding) {
                            inputTitle.setText(note.title)
                            inputDesc.setText(note.description)

                            btnSave.setOnClickListener {
                                // Save the updated note and return to MainActivity
                                val updatedNote = Note(
                                    noteId,
                                    inputTitle.text.toString(),
                                    inputDesc.text.toString(),
                                    getCurrentDate(),
                                    getCurrentTime()
                                )
                                updateNoteAndReturn(updatedNote)
                            }
                        }
                    }
                }
            } else {
                // Add note
                btnSave.setOnClickListener {
                    val newNote = Note(
                        title = inputTitle.text.toString(),
                        description = inputDesc.text.toString(),
                        last_updated_date = getCurrentDate(),
                        last_updated_time = getCurrentTime()
                    )
                    insertNoteAndReturn(newNote)
                }
            }
            btnDelete.setOnClickListener {
                if (noteId != -1) {
                    val emptyNote = Note(noteId, "", "", "", "")
                    deleteNoteAndReturn(emptyNote)
                }
            }
        }

    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(Date())
    }
    private fun insertNoteAndReturn(note: Note) {
        try {
            mNotesDao.insert(note)
            Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
            returnToMainActivity()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateNoteAndReturn(note: Note) {
        try {
            mNotesDao.update(note)
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
            returnToMainActivity()
        } catch (e: Exception) {
            Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteNoteAndReturn(note: Note) {
        try {
            mNotesDao.delete(note)
            Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
            returnToMainActivity()
        } catch (e: Exception) {
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show()
        }
    }
    private fun returnToMainActivity() {
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
        finish()
    }
}
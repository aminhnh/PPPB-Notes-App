package com.example.pppbnotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppbapi.NoteAdapter
import com.example.pppbnotesapp.database.Note
import com.example.pppbnotesapp.database.NoteDao
import com.example.pppbnotesapp.database.NoteRoomDatabase
import com.example.pppbnotesapp.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    protected lateinit var executorService : ExecutorService
    private lateinit var mNotesDao : NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = MyExecutorService.executorService
//        executorService = Executors.newSingleThreadExecutor()

        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        getAllNotes()
        with(binding){
            btnAddNote.setOnClickListener {
                val intentToEditActivity = Intent(this@MainActivity, EditActivity::class.java)
                startActivity(intentToEditActivity)
            }
        }
    }
    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) { notes ->
            val adapterNote = NoteAdapter(notes) { note ->

                val intentToEditActivity = Intent(this@MainActivity, EditActivity::class.java)
                intentToEditActivity.putExtra("id", note.id)
                startActivity(intentToEditActivity)
            }
            binding.rvNotes.apply {
                adapter = adapterNote
                layoutManager =LinearLayoutManager(this@MainActivity)

            }
        }
    }
    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}
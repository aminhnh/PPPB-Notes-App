package com.example.pppbnotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppbapi.NoteAdapter
import com.example.pppbnotesapp.database.NoteDao
import com.example.pppbnotesapp.database.NoteRoomDatabase
import com.example.pppbnotesapp.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var executorService : ExecutorService
    private lateinit var mNotesDao : NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = MyExecutorService.executorService

        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        with(binding){
            btnAddNote.setOnClickListener {
                try {
                    val intentToEditActivity = Intent(this@MainActivity, EditActivity::class.java)
                    startActivity(intentToEditActivity)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            inputSearch.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    val keyword = s.toString()
                    getAllNotes(keyword)
                }

            })
        }
        getAllNotes()
    }
    private fun getAllNotes(keyword : String = "") {
        if (keyword == ""){
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
        } else {
            executorService.execute {
                val notes = mNotesDao.searchNotesByKeyword(keyword)
                runOnUiThread {
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
        }
    }
    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}
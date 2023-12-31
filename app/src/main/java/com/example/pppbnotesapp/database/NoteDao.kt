package com.example.pppbnotesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note : Note)

    @Update
    fun update(note : Note)

    @Delete
    fun delete(note : Note)

    @get:Query("SELECT * from note_table ORDER BY id ASC")
    val allNotes : LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun getNoteById(noteId: Int): LiveData<Note>

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :keyword || '%' OR description LIKE '%' || :keyword || '%'")
    fun searchNotesByKeyword(keyword: String): List<Note>
}
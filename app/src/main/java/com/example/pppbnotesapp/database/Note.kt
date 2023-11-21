package com.example.pppbnotesapp.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id : Int = 0,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "description")
    val description : String,

    @ColumnInfo(name = "last_updated_date")
    val last_updated_date : String,

    @ColumnInfo(name = "last_updated_time")
    val last_updated_time: String,

)

package com.example.emi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class Card (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "english_word")
    val engWord: String,

    @ColumnInfo(name = "russian_word")
    val rusWord: String,

//    @ColumnInfo(name = "id_audio_file")
//    val playSound: Int,

    @ColumnInfo(name = "id_image_file")
    val img: Int,

    @ColumnInfo(name = "date_learned_word")
    val date: String?,

    @ColumnInfo(name = "is_idiom")
    val isIdiom: Boolean

)
package com.example.emi.database

import androidx.room.*

@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(entity = Card::class, parentColumns = ["id"], childColumns = ["progress_id"])
    ],
    indices =[Index("card_id"), Index("date_learned_card")]
)
data class Progress (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "progress_id")
    var progressId: Long = 0,

    @ColumnInfo(name = "card_id")
    val cardId: Long,

    // Инициализируются текущим временем(время изучения)
    @ColumnInfo(name = "date_learned_card")
    val dateLearnedCard: Long = System.currentTimeMillis()
        )
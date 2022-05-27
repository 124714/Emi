package com.example.emi.database

import androidx.room.*
import com.example.emi.convertLongToDateString

//const val MS_IN_DAY = 86_400_000
const val MS_IN_SEC = 10_000
@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(entity = Card::class, parentColumns = ["id"], childColumns = ["card_id"])
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
    val dateLearnedCard: String = convertLongToDateString(System.currentTimeMillis())

//    @ColumnInfo(name = "number_cards_stack")
//    val numberCardsStack: Long = dateLearnedCard / MS_IN_DAY
        )

data class CardAndProgress(
    @Embedded val progress: Progress,
    @Relation(
        parentColumn = "card_id",
        entityColumn = "id"
    )
    val card: Card?
)
package com.example.emi.database

import androidx.annotation.DrawableRes
import androidx.room.*

@Entity(tableName = "word_table")
data class Card (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "english_word")
    val engWord: String,

    @ColumnInfo(name = "russian_word")
    val rusWord: String,


//    @ColumnInfo(name = "id_audio_file")
//    val idAudioFile: Int = 0,

    @DrawableRes
    @ColumnInfo(name = "id_image_file")
    val img: Int,

    @ColumnInfo(name = "date_learned_word")
    val date: String?,

    @ColumnInfo(name = "is_idiom")
    val isIdiom: Boolean,

    @ColumnInfo(name = "category")
    val category: String?,

    @ColumnInfo(name = "mark")
    var mark: Boolean = true

) {
    override fun toString(): String {
        return "Card[id: $id, eng: $engWord, mark: $mark]"
    }

}

@Entity(
    tableName = "table_marked_card",
    foreignKeys = [
        ForeignKey(entity = Card::class, parentColumns = ["id"], childColumns = ["id_card"])
    ],
    indices = [Index("id_card")]
)
data class MarkedCard(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_marked_card")
    val idMarkedCard: Long = 0L,
    val id_card: Long,
)

data class CardAndMarkedCard(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_marked_card"
    )
    val markedCard: MarkedCard?
)


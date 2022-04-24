package com.example.emi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Insert
    suspend fun insert(progress: Progress)

//    @Query("SELECT * FROM word_table WHERE id IN (SELECT card_id FROM dates WHERE date = :date) ")
//    fun getCardsFromDate(date: Calendar) : Flow<List<Card>>

    // Получаем прогресс за все время
    @Query("SELECT * FROM progress")
    fun userProgress(): Flow<List<Progress>>

    // Получаем проогресс нужной карточки
    @Query("select * FROM progress where card_id = :key")
    fun getCardProgress(key: Long): Flow<Progress>

}

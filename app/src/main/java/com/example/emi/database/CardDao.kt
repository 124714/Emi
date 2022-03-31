package com.example.emi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.emi.database.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert
    suspend fun insert(card: Card)

    @Update
    suspend fun update(card: Card)

    @Query("SELECT * from word_table WHERE id = :key")
    suspend fun get(key: Long?): Card?

    @Query("SELECT * from word_table ORDER BY id DESC LIMIT :count")
    suspend fun getLastWords(count: Int): Card?

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM word_table")
    fun getAllCards(): Flow<List<Card>>
}
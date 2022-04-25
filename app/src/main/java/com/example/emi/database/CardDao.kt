package com.example.emi.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("SELECT * FROM word_table WHERE is_idiom = :mark")
    fun getIdioms(mark: Boolean): Flow<List<Card>?>

    @Transaction
    @Query("select * from word_table")
    fun getCardsAndMarkedCard(): Flow<List<CardAndMarkedCard>>

    @Query("select mark from word_table where id  = :cardId")
    fun isMarked(cardId: Long): Boolean

}
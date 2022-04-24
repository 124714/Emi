package com.example.emi.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkedCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(markedCard: MarkedCard)

    @Query("delete from table_marked_card where id_card = :id")
    suspend fun delete(id: Long)

    @Query("select * from table_marked_card")
    fun getMarkedCards(): Flow<List<MarkedCard>?>

//    @Query("select * from table_marked_card where id_card = id")
//    fun get()
}
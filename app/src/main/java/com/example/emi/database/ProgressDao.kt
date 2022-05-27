package com.example.emi.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.emi.dayPassed
import kotlinx.coroutines.flow.Flow
import java.util.Date
const val MS_IN_DAY = 86_400_000
@Dao
interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: Progress)

    @Query("delete from progress where card_id = :cardId")
    suspend fun deleteProgress(cardId: Long)


//    @Query("SELECT * FROM word_table WHERE id IN (SELECT card_id FROM dates WHERE date = :date) ")
//    fun getCardsFromDate(date: Calendar) : Flow<List<Card>>

    // Получаем прогресс за все время
    @Query("SELECT * FROM progress")
    fun userProgress(): Flow<List<Progress>>

    // Получаем проогресс нужной карточки
    @Query("select * FROM progress where card_id = :key")
    fun getCardProgress(key: Long): Flow<Progress>

    @Query("select date_learned_card, count(card_id) as amount_card from progress  group by date_learned_card ")
    fun getCardsAndDate(): Flow<List<DateAndAmountCards>>

    @Query("select date_learned_card, count(card_id) as amount_card from progress" +
            " where date_learned_card >= :date1 and date_learned_card <= :date2" +
            " group by date_learned_card order by date_learned_card ")
    fun getCardsAndDateForPeriod(date1: String, date2: String): Flow<List<DateAndAmountCards>>

    @Transaction
    @Query("select * from progress where date_learned_card  = :date" )
    fun getCardsForDate(date: String): Flow<List<CardAndProgress>>

//    @Transaction
//    @Query("select * from progress where date_learned_card > :startDate and date_learned_card < :endDate" )
//    fun getCardsForPeriod(startDate: String, endDate: String): Flow<List<CardAndProgress>>
}

data class DateAndAmountCards(
    @ColumnInfo(name = "date_learned_card")val date: String?,
    @ColumnInfo(name = "amount_card")val amount: Int?,
    )

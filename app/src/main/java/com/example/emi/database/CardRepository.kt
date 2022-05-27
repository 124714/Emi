package com.example.emi.database

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.emi.convertLongToDateString
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

class CardRepository(
    private val wordDao: CardDao,
    private val progressDao: ProgressDao,
    private val markedCardDao: MarkedCardDao
    ) {


    val allWords = wordDao.getAllCards()
    val allIdioms: Flow<List<Card>?> = wordDao.getIdioms(true)
    val userProgress: Flow<List<Progress>> = progressDao.userProgress()
//    val cardProgress: Flow<Progress> = progressDao.getCardProgress(1)
        // Сохранение последней просмотренной карточики в SliderAdapter
    val amountCardsForDate = progressDao.getCardsAndDate()



    fun getCardsForDate(vararg date: String): Flow<List<Card?>> = flow {
        val list = mutableListOf<Flow<List<Card?>>>()
        for (i in date) {
            val flow = progressDao.getCardsForDate(i).map{ card ->
                card
                    .groupBy { a -> a.card }.keys
                    .toList()
            }
            list.add(flow)
        }

        val allFlows = combine(*list.toTypedArray()) {result ->
            result.reduce { acc, cards -> acc.plus(cards) }
        }
        emitAll(allFlows)
    }.flowOn(Dispatchers.IO)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Card) {
        wordDao.insert(word)
    }

    suspend fun insertProgress(progress: Progress) {
        progressDao.insert(progress)
    }

    suspend fun deleteProgress(cardId: Long) {
            withContext(Dispatchers.Default) {
                progressDao.deleteProgress(cardId)
            }
    }


    fun getCardProgress(cardId: Long): Flow<Progress> {
        return progressDao.getCardProgress(cardId)
    }


    suspend fun updateCard(card: Card) {
        withContext(Dispatchers.IO) {
            wordDao.update(card)
        }
    }



    fun getCardsAndDateForPeriod(period: Pair<String, String>) : Flow<List<DateAndAmountCards>> {
        return progressDao.getCardsAndDateForPeriod(period.first, period.second)
    }

}
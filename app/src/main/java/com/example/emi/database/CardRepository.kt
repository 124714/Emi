package com.example.emi.database

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    val allMarkedCard: Flow<List<MarkedCard>?> = markedCardDao.getMarkedCards()
    val cardAndMarkedCard = wordDao.getCardsAndMarkedCard()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Card) {
        wordDao.insert(word)
    }

    suspend fun insertProgress(progress: Progress) {
        progressDao.insert(progress)
    }

    fun getCardProgress(cardId: Long): Flow<Progress> {
        return progressDao.getCardProgress(cardId)
    }

    suspend fun marked(card: Card) {
        markedCardDao.insert(MarkedCard(0, card.id))
    }

    suspend fun unmarked(card: Card) {
        markedCardDao.delete(card.id)
    }


    suspend fun updateCard(card: Card) {
        wordDao.update(card)
    }

    fun isMarked(cardId: Long) = wordDao.isMarked(cardId)




}
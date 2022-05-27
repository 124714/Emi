package com.example.emi

import android.app.Application
import com.example.emi.database.CardRepository
import com.example.emi.database.CardDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

/* Создается экземляр базы данных для приложения */

class CardsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
    private val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { CardDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {CardRepository(database.cardDao, database.progressDao, database.markedCardDao)}
}
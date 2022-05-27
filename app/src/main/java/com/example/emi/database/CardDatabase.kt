package com.example.emi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Card::class, Progress::class, MarkedCard::class], version = 1, exportSchema = false)
abstract class CardDatabase : RoomDatabase() {

    abstract val cardDao: CardDao
    abstract val progressDao: ProgressDao
    abstract val markedCardDao: MarkedCardDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CardDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardDatabase::class.java,
                    "word_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(CardDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class CardDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.cardDao, database.progressDao)

                    }
                }
            }

            suspend fun populateDatabase(cardDao: CardDao, progressDao: ProgressDao) {
                // Delete all content here.
                cardDao.deleteAll()

                // Add sample words.

//                for (i in 0..1000) {
//                    when(i % 7) {
//                        0 -> cardDao.insert(TestData.myCards[0])
//                        1 -> cardDao.insert(TestData.myCards[1])
//                        2 -> cardDao.insert(TestData.myCards[2])
//                        3 -> cardDao.insert(TestData.myCards[3])
//                        4 -> cardDao.insert(TestData.myCards[4])
//                        5 -> cardDao.insert(TestData.myCards[5])
//                        6 -> cardDao.insert(TestData.myCards[6])
//                    }
//                }

                for(i in TestData.myCards){
                    cardDao.insert(i)
                }
                for (i in TestData.progress) {
                    progressDao.insert(i)
                }

//                for (i in 0..1000) {
//                    when(i % 6) {
//                        0 -> progressDao.insert(TestData.progress[0])
//                        1 -> progressDao.insert(TestData.progress[1])
//                        2 -> progressDao.insert(TestData.progress[2])
//                        3 -> progressDao.insert(TestData.progress[3])
//                        4 -> progressDao.insert(TestData.progress[4])
//                        5 -> progressDao.insert(TestData.progress[5])
//                    }
//                }

                // TODO: Add your own words!
            }
        }

    }
}

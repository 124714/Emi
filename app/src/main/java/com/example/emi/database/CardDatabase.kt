package com.example.emi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.emi.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Card::class], version = 1, exportSchema = false)
abstract class CardDatabase : RoomDatabase() {

    abstract val cardDao: CardDao

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
                        var cardDao = database.cardDao
                        populateDatabase(database.cardDao)
                    }
                }
            }

            suspend fun populateDatabase(cardDao: CardDao) {
                // Delete all content here.
                cardDao.deleteAll()

                // Add sample words.

                for (i in 0..100) {
                    when(i % 3) {
                        0 -> cardDao.insert(Card( 0,"lemon", "лимон", R.drawable.lemon, "понедельник"))
                        1 -> cardDao.insert(Card( 0,"apple", "яблоко", R.drawable.apple, "вторник"))
                        2 -> cardDao.insert(Card( 0,"orange", "апельсин", R.drawable.orange, "среда"))
//                        3 -> cardDao.insert(Card( 0,"dog", "собака", R.drawable.dog))
//                        else -> cardDao.insert(Card( 0,"switch", "переключатель, тумблер", R.drawable.toggle))
                    }
                }
                // TODO: Add your own words!
            }
        }

    }
}

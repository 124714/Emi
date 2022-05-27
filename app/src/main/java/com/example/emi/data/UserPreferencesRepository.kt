package com.example.emi.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
)


data class UserPreferences(
    val startPositionViewPager: Int
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKey {
        val START_POSITION_VIEW_PAGER = intPreferencesKey("start_position")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Timber.e("Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Извлекаем позицию из настроек
        val position = preferences[PreferencesKey.START_POSITION_VIEW_PAGER] ?: 0
        return UserPreferences(position)
    }

    suspend fun fetchInitialPreferences() : UserPreferences {
        dataStore.data.map {preferences ->

        }
//        Timber.i("DATA_STORE: ${dataStore.data.first().toPreferences()}")
       return mapUserPreferences(dataStore.data.first().toPreferences())
    }
    suspend fun updateStartPosition(position: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.START_POSITION_VIEW_PAGER] = position
        }
    }

}
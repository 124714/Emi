package com.example.emi.data
/** [https://developer.android.com/codelabs/basic-android-kotlin-training-preferences-datastore?hl=en#4] **/


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

/**
 * Class that handles saving and retrieving layout setting preferences
 */

private const val SCROLL_PREFERENCES_NAME = "scroll_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SCROLL_PREFERENCES_NAME
)

class SettingsDataStore(preference_datastore: DataStore<Preferences>) {
    // Создание ключа в котором хранится значения состояния макета
//    private val IS_LINEAR_LAYOUT_MANAGER = booleanPreferencesKey("is_linear_layout_manager")
    private val stateViewPager = intPreferencesKey("stateViewPager")

    val preferenceFlow: Flow<Int> = preference_datastore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            // значение по умолчанию
            Timber.i("Settings: ${preferences[stateViewPager]}")
            preferences[stateViewPager] ?: 0
        }

    suspend fun saveLayoutToPreferencesStore(scrollPosition: Int, context: Context) {
//        Timber.i("scrollPostion: $scrollPosition")
        context.dataStore.edit { preferences ->
            preferences[stateViewPager] = scrollPosition
        }
    }
}
package com.example.emi.ui.sliderRepeat

import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*

import com.example.emi.combineAndCompute
import com.example.emi.data.UserPreferences
//import com.example.emi.data.SettingsDataStore
import com.example.emi.data.UserPreferencesRepository
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import com.example.emi.database.Progress
import com.example.emi.database.TestData
import com.example.emi.toFilter
import com.example.emi.toFilteredList
import com.example.emi.toList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

data class CardListModel(
        val cards: List<Card>,
        val startPosition: Int
)

class SliderRepeatViewModel(
        val repository: CardRepository,
        private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

        private lateinit var filter: Filter
        private val _cardAndDate = MutableLiveData<List<Card?>>()
        val cardAndDate: LiveData<List<Card?>>
                get() = _cardAndDate

        private val _listWord = MutableLiveData<MutableList<String>>()
        val listWord: LiveData<MutableList<String>>
                get() = _listWord

        fun addCardsForDate(vararg dates: String) {
                viewModelScope.launch {
                        val a = repository.getCardsForDate(*dates).first()
                        Timber.i("addCardsForRepeat: $a")
                        _cardAndDate.value = a
                        _listWord.value = a.map{word -> word?.engWord} as MutableList<String>
                        filter = Filter(a.toFilter())

                }
        }

        fun onChangeFilter(s: String, checked: Boolean) {
                filter.update(s, checked)
                _cardAndDate.value = filter.getVisibleCards()

        }

//        private lateinit var filter: MutableMap<String, Boolean>


        private class Filter(val filter: MutableMap<Card, Boolean>) {
                fun update(s: String, checked: Boolean) {
                        for(i in filter) {
                                if (i.key.engWord == s)
                                        filter[i.key] = checked
                        }
                }

                fun getVisibleCards(): List<Card> {
                        return (filter.toFilteredList() as List<Card>)
                }



        }
}

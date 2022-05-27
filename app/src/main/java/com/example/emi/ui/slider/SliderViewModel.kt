package com.example.emi.ui.slider

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
import com.example.emi.toFilteredList
import com.example.emi.toList
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

data class CardListModel(
    val cards: List<Card>,
    val startPosition: Int
)

class SliderViewModel(
    val repository: CardRepository,
    private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

//    val cardAndDate = repository.getCardsForDate("09-мая-2022","10-мая-2022").asLiveData()

    private var filter = CardFilter()
    val allProgress: LiveData<List<Progress>> = repository.userProgress.asLiveData()

//    val allProgress: LiveData<List<Progress>> = repository.Progress.asLiveData()
    fun updateProgress(progress: Progress, mark: Boolean) = viewModelScope.launch {
        when(mark) {
            true -> repository.insertProgress(progress)
            false -> repository.deleteProgress(progress.cardId)
        }
    }
    //------------------------------------------------------------------
    // Только что добаленные карточки
    private val cardsForRepeat = mutableListOf<Card>()
//    private val filter = mutableMapOf<Card, Boolean>()

    // chips
    private val _cardList = MutableLiveData<MutableList<Card>>()
    private val _justAddedCard = MutableLiveData<MutableList<Card>>()
    init {
        onDataChanged()
    }

    val cardList: LiveData<MutableList<Card>>
        get() = _cardList
    val justAddedCard: LiveData<MutableList<Card>>
        get() = _justAddedCard

    fun onChangeFilter(card: Card, isChecked: Boolean){
//        filter.initFilter(card)
        filter.update(card, isChecked)
        _justAddedCard.value = filter.getCardsForFilter()

    }

    private fun onDataChanged() {
        _justAddedCard.value = filter.getCardsForFilter()
        _cardList.value = filter.getFilters()
//        Timber.i("onDataChanged: JAC ${_justAddedCard.value} CL ${_cardList.value}")
    }



    // Только что добавленные карточки из SliderAdapter
    private fun addCardForRepeat(card: Card) {
        if (card !in cardsForRepeat) {
            cardsForRepeat.add(card)
//            filter[card] = false
        }
    }

    fun updateCard(card: Card) {
        viewModelScope.launch {
            val markedCard = card.copy().apply{mark = !mark}
            repository.updateCard(markedCard)
        }
    }

    fun updateFilter(card: Card) {
//        Timber.i("updateFilter: $card")
        filter.initFilter(card)
        onDataChanged()
    }
    // -----------------------------------------------------------

    // Добавляем карточку в базу данных
    fun insert(word: Card) =
        viewModelScope.launch { repository.insert(word)}

    override fun onCleared() {
        super.onCleared()

    }

    companion object {
        const val ALL_CARDS = 0
        const val LAST_ADDED_CARD = 1
    }

    // ------------------------------------------------------------------
    fun saveLastPosition() {
        viewModelScope.launch {
            userPreferencesRepository.updateStartPosition(_position)
        }
    }


    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow
    private val cardListModelFlow = combine(
        repository.allWords,
        userPreferencesFlow
    ) { list: List<Card> ,  userPrefs: UserPreferences ->
        return@combine CardListModel(
            cards = list,
            startPosition = userPrefs.startPositionViewPager
        )
    }
    val cardListModel = cardListModelFlow.asLiveData()



    private var _position = 0
    val position: Int
        get() = _position

    fun savePosition(pos: Int){
        _position = pos
    }


    val initialSetupEvent = liveData {
        emit(userPreferencesRepository.fetchInitialPreferences())
    }



    private class CardFilter {
        val currentValue = mutableMapOf<Card, Boolean>()
        fun initFilter(card: Card) {
            Timber.i("initFilter")
            if(card.mark) {
                Timber.i("IF1: $card")
                currentValue[card] = false

            }
            if(!card.mark ) {
                if(card.apply { mark = !mark } in currentValue) {
                    Timber.i("abc")
                    currentValue.remove(card)

                }
            }
        }

        fun update(changeFilter: Card, isChecked: Boolean): Boolean {
            currentValue[changeFilter] = isChecked

            return true
        }

        fun getCardsForFilter(): MutableList<Card> {
            return currentValue.toFilteredList()
        }

        fun getFilters(): MutableList<Card> {
            return currentValue.toList()
        }

    }

    var isScrollUpdate = true




}
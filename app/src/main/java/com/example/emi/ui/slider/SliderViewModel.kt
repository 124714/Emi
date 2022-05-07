package com.example.emi.ui.slider

import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.example.emi.combineAndCompute
import com.example.emi.data.UserPreferences
//import com.example.emi.data.SettingsDataStore
import com.example.emi.data.UserPreferencesRepository
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
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



    //------------------------------------------------------------------
    // Только что добаленные карточки
    private val cardsForRepeat = mutableListOf<Card>()
    private val filter = mutableMapOf<Card, Boolean>()

    // chips
    private val _cardList = MutableLiveData(cardsForRepeat)
    private val _justAddedCard = MutableLiveData(cardsForRepeat)


    val cardList: LiveData<MutableList<Card>>
        get() = _cardList
    val justAddedCard: LiveData<MutableList<Card>>
        get() = _justAddedCard

    fun onChangeFilter(card: Card, isChecked: Boolean){
        filter[card] = isChecked
        _justAddedCard.postValue(filter.toFilteredList())
    }


    private fun MutableMap<Card, Boolean>.toFilteredList(): MutableList<Card> {
        val list = mutableListOf<Card>()
        for (i in this){
            if(!i.value) {
                list.add(i.key)
            }
        }
        return list
    }

    // Только что добавленные карточки из SliderAdapter
    private fun addCardForRepeat(card: Card) {
        if (card !in cardsForRepeat) {
            cardsForRepeat.add(card)
            filter[card] = false
        }
    }

    fun updateCard(card: Card) {
        viewModelScope.launch {
            val markedCard = card.copy().apply{mark = !mark}
            repository.updateCard(markedCard)
            //***************************
            if(markedCard.mark) {
                addCardForRepeat(card)
            }
            if (markedCard in cardsForRepeat && !markedCard.mark) {
                cardsForRepeat.remove(markedCard)
                filter[markedCard] = false
            }
        }
    }
    // -----------------------------------------------------------

    // Добавляем карточку в базу данных
    fun insert(word: Card) =
        viewModelScope.launch { repository.insert(word)}

    override fun onCleared() {
        super.onCleared()
//        Timber.i("SliderViewModel destroyed")
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
        val filter = mutableMapOf<Card, Boolean>()
        fun update(changeFilter: Card, isChecked: Boolean) {
            if(isChecked) {

            }
        }


    }


}
package com.example.emi.ui.slider

import androidx.lifecycle.*
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import com.example.emi.database.Progress
import com.example.emi.database.TestData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber

class SliderViewModel(val repository: CardRepository,) : ViewModel() {

    fun isMarked(cardId: Long) : Boolean{
        return repository.isMarked(cardId)
    }
    private var _fabButtonEvent = MutableLiveData(false)
    val fabButtonEvent: LiveData<Boolean>
        get() = _fabButtonEvent

    fun fabButtonIsClicked() {
        _fabButtonEvent.value = !_fabButtonEvent.value!!
    }

//    fun fabButtonDoneClicked() {
//        _fabButtonEvent.value = false
//    }

//    var cardId: Long? = null

    // Все карточки из БД
    val allCards: LiveData<MutableList<Card>> = repository.allWords

    val _aCards = MutableLiveData<MutableList<Card>>(allCards.value)
    val aCards: LiveData<MutableList<Card>>
        get() = _aCards

    // Весь прогресс из БД
    val allProgress: LiveData<List<Progress>> = repository.userProgress.asLiveData()

    val progress = repository.getCardProgress(1).asLiveData()

    // отмеченные карточки
    val allMarkedCards = repository.allMarkedCard.asLiveData()

    val allCardAndMarkedCard = repository.cardAndMarkedCard.asLiveData()

    // Только что добаленные карточки
    private val _justAddedCard = MutableLiveData<MutableList<Card>>()
    val justAddedCard: LiveData<MutableList<Card>>
        get() = _justAddedCard


    fun addCard() {
        val currentList = _aCards.value
        currentList?.add(TestData.myCards[0])
        _aCards.postValue(currentList)
    }
    // Только что добавленные карточки из SliderAdapter
    fun addCardFromSlider(cards: MutableList<Card>) {
        _justAddedCard.value = cards

    }

    fun updateCard(card: Card) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCard(card)
    }

    // Добавляем карточку в базу данных
    fun insert(word: Card) = viewModelScope.launch {
        repository.insert(word)
    }

    fun setMark(card: Card) {
       viewModelScope.launch(Dispatchers.IO) {
            repository.marked(card)
        }
    }

    fun unmark(card: Card) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unmarked(card)
        }
    }


//    fun isMarked(cardId: Long): Boolean {
//        return when(allMarkedCards.value?.find{ mCard -> mCard.id_card == cardId }) {
//            null -> false
//            else -> true
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("SliderViewModel destroyed")
    }

    companion object {
        const val ALL_CARDS = 0
        const val LAST_ADDED_CARD = 1
    }
}
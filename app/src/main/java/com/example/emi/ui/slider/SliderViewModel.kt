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
    val allCards: LiveData<List<Card>> = repository.allWords.asLiveData()

    val _aCards = MutableLiveData<List<Card>>(allCards.value)
    val aCards: LiveData<List<Card>>
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

    override fun onCleared() {
        super.onCleared()
        Timber.i("SliderViewModel destroyed")
    }

    companion object {
        const val ALL_CARDS = 0
        const val LAST_ADDED_CARD = 1
    }
}
package com.example.emi.ui.slider

import androidx.lifecycle.*
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import kotlinx.coroutines.launch

class SliderViewModel(val repository: CardRepository,) : ViewModel() {

    val allCards: LiveData<List<Card>> = repository.allWords.asLiveData()


    fun insert(word: Card) = viewModelScope.launch {
        repository.insert(word)
    }
}
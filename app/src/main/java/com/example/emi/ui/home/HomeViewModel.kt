package com.example.emi.ui.home

import androidx.lifecycle.*
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import kotlinx.coroutines.launch

class HomeViewModel(val repository: CardRepository,) : ViewModel() {

    val allCards: LiveData<List<Card>> = repository.allWords.asLiveData()

    val allIdioms: LiveData<List<Card>?> = repository.allIdioms.asLiveData()

    fun insert(word: Card) = viewModelScope.launch {
        repository.insert(word)
    }
}
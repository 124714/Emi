package com.example.emi.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import kotlinx.coroutines.launch

class HomeViewModel(val repository: CardRepository,) : ViewModel() {

    val allCards: LiveData<List<Card>> = repository.allWords.asLiveData()

    val allIdioms: LiveData<List<Card>?> = repository.allIdioms.asLiveData()

    val _category = MutableLiveData<List<String>>()
    val category: LiveData<List<String>>
        get() =_category

    init {
        _category.value = listOf("животные", "эмоции", "время", "цвета", "одиннадацать","игра")
    }

    fun insert(word: Card) = viewModelScope.launch {
        repository.insert(word)
    }

}
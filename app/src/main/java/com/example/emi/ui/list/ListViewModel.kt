package com.example.emi.ui.list


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import kotlinx.coroutines.launch

class ListViewModel(val repository: CardRepository,) : ViewModel() {

    val allCards: LiveData<MutableList<Card>> = repository.allWords

    val allIdioms: LiveData<List<Card>?> = repository.allIdioms.asLiveData()

    val _category = MutableLiveData<List<String>>()
    val category: LiveData<List<String>>
        get() =_category



    fun insert(word: Card) = viewModelScope.launch {
        repository.insert(word)
    }

    fun hello() {
        Log.i("homeView", "hello")
    }


}
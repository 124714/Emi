package com.example.emi.ui.list


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import kotlinx.coroutines.launch

class ListViewModel(val repository: CardRepository,) : ViewModel() {

    val allCards: LiveData<List<Card>> = repository.allWords.asLiveData()

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

    private val _navigateToSliderFragment = MutableLiveData<Int?>()
    val navigateToSliderFragment: LiveData<Int?>
        get() = _navigateToSliderFragment

    fun onListItemClicked(pos: Int) {
        _navigateToSliderFragment.value = pos
    }
//
//    fun onSliderFragmentNavigated() {
//        _navigateToSliderFragment.value = null
//    }

    fun doneNavigating() {
        _navigateToSliderFragment.value = null
    }

}
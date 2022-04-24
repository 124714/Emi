package com.example.emi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emi.database.CardRepository

class ListViewModelFactory(private val repository: CardRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ListViewModel::class.java)) @Suppress("UNCHECKED_CAST")
        return ListViewModel(repository) as T
        throw(java.lang.IllegalArgumentException("UnknownViewModelClass"))
    }
}
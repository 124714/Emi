package com.example.emi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emi.database.CardRepository


class HomeViewModelFactory(private val repository: CardRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) @Suppress("UNCHECKED_CAST")
        return HomeViewModel(repository) as T
        throw(java.lang.IllegalArgumentException("UnknownViewModelClass"))
    }
}
package com.example.emi.ui.slider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emi.database.CardRepository


class SliderViewModelFactory(private val repository: CardRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SliderViewModel::class.java)) @Suppress("UNCHECKED_CAST")
        return SliderViewModel(repository) as T
        throw(java.lang.IllegalArgumentException("UnknownViewModelClass"))
    }
}
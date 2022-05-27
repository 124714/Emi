package com.example.emi.ui.slider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import com.example.emi.data.SettingsDataStore
import com.example.emi.data.UserPreferencesRepository
import com.example.emi.database.CardRepository


class SliderViewModelFactory(
    private val repository: CardRepository,
    private val userPreferencesRepository: UserPreferencesRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SliderViewModel::class.java)) @Suppress("UNCHECKED_CAST")
        return SliderViewModel(repository, userPreferencesRepository) as T
        throw(java.lang.IllegalArgumentException("UnknownViewModelClass"))
    }
}
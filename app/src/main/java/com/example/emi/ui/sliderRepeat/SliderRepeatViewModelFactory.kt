package com.example.emi.ui.sliderRepeat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import com.example.emi.data.SettingsDataStore
import com.example.emi.data.UserPreferencesRepository
import com.example.emi.database.CardRepository
import com.example.emi.ui.slider.SliderViewModel


class SliderRepeatViewModelFactory(
    private val repository: CardRepository,
    private val userPreferencesRepository: UserPreferencesRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SliderRepeatViewModel::class.java)) @Suppress("UNCHECKED_CAST")
        return SliderRepeatViewModel(repository, userPreferencesRepository) as T
        throw(java.lang.IllegalArgumentException("UnknownViewModelClass"))
    }
}
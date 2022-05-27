package com.example.emi.ui.home


import androidx.lifecycle.*
import com.example.emi.buildStringForDB
import com.example.emi.convertDateToLong
import com.example.emi.convertLongToDateString
import com.example.emi.database.Card
import com.example.emi.database.CardRepository
import com.example.emi.database.DateAndAmountCards
import com.example.emi.database.Progress
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

class HomeViewModel(val repository: CardRepository,) : ViewModel() {

    val dates = mutableMapOf<String, Boolean>()
    val allIdioms: LiveData<List<Card>?> = repository.allIdioms.asLiveData()
    private val _btnShowEvent = MutableLiveData<Boolean>()
    private val _selectedCardsForDate = MutableLiveData<Map<String, Boolean>>()





    val btnShowEvent: LiveData<Boolean>
        get() = _btnShowEvent

    val selectedCardsForDate: LiveData<Map<String, Boolean>>
        get() = _selectedCardsForDate

    fun btnShowClicked() {
        _btnShowEvent.value = true
    }

    fun doneNavigating() {
        _btnShowEvent.value = null
    }

    fun getDatesForRepeat(): Array<String> {
        return dates.filter{date -> date.value }.keys.toTypedArray()
    }
    fun getFilterStatus(): String {
        return filter.getFilterStatus()
    }

    private val filter = Filter()
    class Filter {
        private var end: String? = null
        private var start: String? = null
        var currentValue: Int? = null

        fun updateFilter(changedFilter: Int, isChecked: Boolean) : Boolean {
            if (isChecked) {
                currentValue = changedFilter
                return true
            } else if(currentValue == changedFilter) {
                currentValue = ALL_CARDS
                return true
            }
            return false
        }

        fun initPeriod(d1: String?, d2: String?): Boolean {
            if (d1 != start && d2 != end) {
                end = d1
                start = d2
                return true
            }
            return false
        }
        fun getPeriod(): Pair<String, String> {
            return Pair(end!!, start!!)
        }
        fun getThreeDays(): Pair<String, String> {
            val end = System.currentTimeMillis()
            val start = end - THREE_DAYS
            this.end = convertLongToDateString(end)
            this.start = convertLongToDateString(start)
            return Pair(this.start!!, this.end!!)
        }
        fun getWeek(): Pair<String, String> {
            val end = System.currentTimeMillis()
            val start = end - WEEK
            this.end = convertLongToDateString(end)
            this.start = convertLongToDateString(start)
            return Pair(this.start!!, this.end!!)

        }
        fun getMonth(): Pair<String, String> {
            val end = System.currentTimeMillis()
            val start = end - MONTH
            this.end = convertLongToDateString(end)
            this.start = convertLongToDateString(start)
            return Pair(this.start!!, this.end!!)

        }

        fun getFilterStatus(): String {
            return "$start:$end"
        }

    }

//    val amountCardsAndDates = repository.amountCardsForDate.asLiveData()

    private val _amountCardsAndDates = MutableLiveData<List<DateAndAmountCards>>()
    val amountCardsAndDates: LiveData<List<DateAndAmountCards>>
        get() = _amountCardsAndDates


    init {
        onDataChanged(ALL_CARDS)
    }

    private fun onDataChanged(tag: Int) {
        viewModelScope.launch {
            when (tag) {
                ALL_CARDS -> _amountCardsAndDates.value = repository.amountCardsForDate.first()
                CARDS_FOR_PERIOD -> _amountCardsAndDates.value = repository
                        .getCardsAndDateForPeriod(filter.getPeriod()).first()
                CARDS_FOR_THREE_DAY -> {_amountCardsAndDates.value =
                    repository
                        .getCardsAndDateForPeriod(filter.getThreeDays()).first()


                }

                CARDS_FOR_WEEK -> _amountCardsAndDates.value = repository
                    .getCardsAndDateForPeriod(filter.getWeek()).first()

                CARDS_FOR_MONTH -> _amountCardsAndDates.value = repository
                    .getCardsAndDateForPeriod(filter.getMonth()).first()
            }
        }
    }

    fun onFilterChanged(tag: Int, isChecked: Boolean) {
        if(filter.updateFilter(tag, isChecked)) {
            filter.currentValue?.let { onDataChanged(it) }
        }
    }

    fun updateFilterForPeriod(tag: Int, isChecked: Boolean) {
        filter.updateFilter(tag, isChecked)
    }

    fun onFilterChangeForPeriod(datePeriod: String) {
        val dates = buildStringForDB(datePeriod)
        Timber.i("$dates")
        if(initPeriod(dates.first, dates.second)) {
            filter.currentValue?.let { onDataChanged(it) }
        }
    }



    fun initPeriod(start: String, end: String): Boolean{
       return filter.initPeriod(start, end)
    }

    fun insert(word: Card) = viewModelScope.launch {
        repository.insert(word)
    }

    fun setDates(date: String, checked: Boolean) {
        _selectedCardsForDate.value = dates
    }

    companion object {
        const val ALL_CARDS           = 0
        const val CARDS_FOR_PERIOD    = 1
        const val CARDS_FOR_THREE_DAY = 2
        const val CARDS_FOR_WEEK      = 3
        const val CARDS_FOR_MONTH     = 4

        const val THREE_DAYS = 2 * 86_400_000L
        const val WEEK       = 6 * 86_400_000L
        const val MONTH      = 29 * 86_400_000L
    }

}
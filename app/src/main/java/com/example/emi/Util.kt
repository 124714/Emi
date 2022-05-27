package com.example.emi

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.emi.database.Card
import com.example.emi.database.TestData
import timber.log.Timber
import java.sql.Date
import java.text.SimpleDateFormat

const val DAY = 3600000*24

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("yyyy-MM-dd")
        .format(systemTime).toString()

}

fun populateCards(stringArray: Array<String>) {
    val a = mutableListOf<Card?>()
    a.add(TestData.myCards[0])

    for (data in stringArray) {

    }
}

fun <T, A, B> LiveData<A>.combineAndCompute(other: LiveData<B>, onChange: (A, B) -> T): MediatorLiveData<T> {


    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value

        if (source1emitted && source2emitted) {
            result.value = onChange.invoke(source1Value!!, source2Value!! )
        }
    }

    result.addSource(this) { source1emitted = true; mergeF.invoke() }
    result.addSource(other) { source2emitted = true; mergeF.invoke() }

    return result
}

fun MutableMap<Card, Boolean>.toFilteredList(): MutableList<Card> {
    val list = mutableListOf<Card>()
    for (i in this){
        if(!i.value) {
            list.add(i.key)
        }
    }
    return list
}
fun MutableMap<Card, Boolean>.toList(): MutableList<Card> {
    val list = mutableListOf<Card>()
    for (i in this){
        list.add(i.key)
    }
    return list
}

//fun convertLongToDateString(systemTime: Long): String {
//    return SimpleDateFormat("EEEE dd-MMMM-yyyy' Время: 'HH:mm")
//        .format(systemTime).toString()
//
//}

fun convertDateToLong(date: String): Long {
    return SimpleDateFormat("yyyy-MM-dd").parse(date).time
}

fun String.dayPassed(): Int {
    val currentTime = System.currentTimeMillis()
    return ((currentTime - convertDateToLong(this)) / 86_400_000 ).toInt()
}

fun dayPassed(s: String): String {

    val currentTime = System.currentTimeMillis()
    val day = ((currentTime - convertDateToLong(s)) / 86_400_000).toInt()

    return getNumEnding(day)
}

fun List<Card?>.toFilter(): MutableMap<Card, Boolean> {
    val filter = mutableMapOf<Card, Boolean>()
    for(i in this) {
        filter[i!!] = false
    }
    return filter
}

fun getNumEnding(number: Int): String {
    // https://gist.github.com/seredniy/bbdc3ff594719b9a65e42f3d708a406a
    val days = arrayOf("день", "дня", "дней")
    val number = number % 100
    when(number) {
        1 -> return "вчера"
        0 -> return "сегодня"
    }

    return if (number in 11..19) {
         "$number ${days[2]} назад"
    }else {
        when(number % 10) {
            1 -> "$number ${days[0]} назад"
            in (2..4) -> "$number ${days[1]} назад"
            else -> "$number ${days[2]} назад"
        }
    }
}


fun buildStringForDB(str: String): Pair<String, String>{
    val strings = str.split(":")
    val start = strings[0]
    val end = strings[1]

    val start2 = start.split("-").reversed().joinToString("-")
    val end2 = end.split("-").reversed().joinToString("-")
    return Pair(start2, end2)
}


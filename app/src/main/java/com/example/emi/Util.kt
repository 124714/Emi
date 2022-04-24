package com.example.emi

import android.annotation.SuppressLint
import com.example.emi.database.Card
import com.example.emi.database.TestData
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE dd-MMMM-yyyy' Время: 'HH:mm")
        .format(systemTime).toString()

}

fun populateCards(stringArray: Array<String>) {
    val a = mutableListOf<Card?>()
    a.add(TestData.myCards[0])

    for (data in stringArray) {

    }
}


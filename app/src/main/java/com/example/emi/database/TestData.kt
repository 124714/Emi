package com.example.emi.database

import android.annotation.SuppressLint
import com.example.emi.R

object TestData {

    val myCards = listOf(
        Card( 0,
            "lemon",
            "лимон",
            R.drawable.lemon,

            "понедельник",false,
            "фрукты",),
        Card( 0,
            "apple",
            "яблоко",
            R.drawable.apple,

            "вторник",
            false,
            "фрукты",),
        Card( 0,
            "all in good time",
            "всему свое время",
            R.drawable.all_in_good_time,

            null,
            true,
            "время",
            ),
        Card( 0,
            "blow one's mind",
            "потрясти, шокировать",
            R.drawable.blow_ones_mind,

            null,
            true,
            "эмоции",
            ),
        Card( 0, "chicken out", "трусить", R.drawable.orange,
            null, true, "животные"),
        Card( 0, "cloudy", "облачно", R.drawable.cloudy,
            null, true, "животные", ),
        Card( 0, "abroad", "за границу", R.drawable.abroad,
            null, true, "животные", ),
        Card( 0, "behaviour", "поведение", R.drawable.behavior,
            null, true, "животные", ),
        Card( 0, "benefits", "преимущество", R.drawable.benefits,
            null, true, "животные", ),
        Card( 0, "enhanced", "улучшенный", R.drawable.enhanced,
            null, true, "животные", ),
        Card( 0, "fit", "подходить, соответствовать", R.drawable.fit,
            null, true, "животные", ),
        Card( 0, "pick", "выбирать", R.drawable.pick,
            null, true, "животные", ),
        Card( 0, "resemble", "надёжный", R.drawable.resemble,
            null, true, "животные", ),
        Card( 0, "responsive", "отзывчивый", R.drawable.responsive,
            null, true, "животные", ),
        Card( 0, "scrunched", "сморщенный", R.drawable.scrunched,
            null, true, "животные", ),

    )

    val progress = listOf(
        Progress(0, 1, "2022-05-19"),
        Progress(0, 2, "2022-05-18"),
        Progress(0, 3, "2022-05-17"),
        Progress(0, 4, "2022-05-16"),
        Progress(0, 5, "2022-05-15"),
        Progress(0, 6, "2022-05-14"),
        Progress(0, 7, "2022-05-13"),
        Progress(0, 8, "2021-07-13"),
        Progress(0, 9, "2021-01-14"),
        Progress(0, 10, "2021-01-14"),
        Progress(0, 11, "2021-01-14"),
        Progress(0, 12,"2021-11-15"),
        Progress(0, 13, "2021-05-12"),
        Progress(0, 14,"2021-11-11"),
        Progress(0, 15, "2021-11-11"),

    )

    val progress2 = listOf(
        ProgressTest("28.02.2022", "понедельник", 8, "10 дней назад"),
        ProgressTest("17.02.2022", "вторник", 12, "8 дней назад"),
        ProgressTest("14.05.2022", "среда", 10, "1 день назад"),
        ProgressTest("16.02.2022", "четверг", 11, "5 дней назад"),
        ProgressTest("8.02.2022", "пятница", 15, "34 дней назад"),
        ProgressTest("19.02.2022", "суббота", 9, "128 дней назад"),
        ProgressTest("13.02.2022", "воскресенье", 10, "1 день назад"),
        ProgressTest("31.02.2022", "понедельник", 7, "2 дня назад"),
        ProgressTest("25.02.2022", "понедельник", 11, "13 дней назад"),
    )

}

data class ProgressTest(
    val date: String,
    val dayOfWeak: String,
    val numCards: Int,
    val dayPassed: String
)


package com.example.emi.database

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
            "orange",
            "апельсин",
            R.drawable.orange,
            "среда",
            false,
            "фрукты",
            ),
        Card( 0,
            "all in good time",
            "всему свое время",
            R.drawable.orange,
            null,
            true,
            "время",
            ),
        Card( 0,
            "blow one's mind",
            "потрясти, шокировать",
            R.drawable.orange,
            null,
            true,
            "эмоции",
            ),
        Card( 0, "chicken out", "трусить", R.drawable.orange,
            null, true, "животные", )

    )

    val progress = listOf(
        Progress(0, 1),
        Progress(0, 2),
        Progress(0, 3),
        Progress(0, 4),
        Progress(0, 4),
        Progress(0, 6),
    )
}
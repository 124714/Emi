package com.example.emi.ui.home.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.emi.R
import com.example.emi.database.Card

@BindingAdapter("categoryImage")
fun ImageView.setCategoryImage(item: Card?){
    item?.let {
        setImageResource(when(item.category) {
            "время" -> R.drawable.time
            "эмоции" -> R.drawable.emotions
            else -> R.drawable.animals
        })
    }
}

@BindingAdapter("inMyVocabulary")
fun ImageView.setInMyVocabluery(item: Card?) {
    item?.let {
        setImageResource(when(item.mark) {
            true -> R.drawable.star
            else -> R.drawable.star_filled
        })
    }
}
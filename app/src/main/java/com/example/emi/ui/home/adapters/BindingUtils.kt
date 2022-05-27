package com.example.emi.ui.home.adapters

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.emi.R
import com.example.emi.animation.ViewAnimation
import com.example.emi.database.Card
import com.example.emi.ui.slider.SliderAdapter
import com.example.emi.ui.slider.SliderViewModel

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
fun ImageView.setInMyVocabulery(viewModel: SliderViewModel) {
//    item?.let {
//        setImageResource(when(item.mark) {
//            true -> R.drawable.star
//            else -> R.drawable.star_filled
//        })
//    }


}

@BindingAdapter("cardText")
fun TextView.setCardText(item: Card) {
    when(id) {
        R.id.eng_word -> text = item.engWord
        R.id.rus_word -> text = item.rusWord
    }
}

@BindingAdapter("cardId")
fun TextView.setCardId(item: Card) {
    item.let {
        text = item.id.toString()
    }
}

@BindingAdapter("cardImage")
fun ImageView.setCardImage(item: Card) {
    setImageResource(item.img)
}

@BindingAdapter("cardMark")
fun ImageButton.setCardMark(card: Card) {
    when(card.mark) {
        true -> {
//            ViewAnimation.scaler(this, this)
            setImageResource(R.drawable.star_filled)
        }
        false -> {
//            ViewAnimation.scaler(this, this)
            setImageResource(R.drawable.star)
        }
    }
}



@BindingAdapter("markedCard")
fun ImageView.setCardMark(card: Card) {
    when(card.mark) {
        true -> this.visibility = View.VISIBLE
        else -> this.visibility = View.GONE
    }
}


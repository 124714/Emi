package com.example.emi.ui.slider

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.R
import com.example.emi.animation.ViewAnimation
import com.example.emi.database.Card
import com.example.emi.databinding.SliderCardItem2Binding
import com.example.emi.databinding.SliderCardItemBinding


class SliderAdapter(
    private val clickListener: StarButtonListener,
    private val clickListener2: AudioBtnListener
)
    : ListAdapter<Card, RecyclerView.ViewHolder>(SliderDiffCallBack) {

    companion object SliderDiffCallBack : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = getItem(position)
        (holder as CardViewHolder).bind(card, clickListener, clickListener2)

    }

    class CardViewHolder private constructor(private val binding: SliderCardItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Card, clickListener: StarButtonListener, clickListener2: AudioBtnListener) {
            with(binding) {
                card = item
                this.clickListener = clickListener
                this.clickListener2 = clickListener2
                executePendingBindings()
            }
        }
        init {
//            setupFlipCardListener()
        }

//        private fun setupFlipCardListener() {
//            binding.flipCard.setOnClickListener{
//                ViewAnimation.rotater(binding.flippedCard, binding.flipCard)
//                when(binding.engWord.isVisible){
//                    true -> {
//                        binding.apply{
//                            engWord.visibility = View.INVISIBLE
//                            rusWord.visibility = View.VISIBLE
//                        }
//                    }
//                    false -> {
//                        binding.apply{
//                            engWord.visibility = View.VISIBLE
//                            rusWord.visibility = View.INVISIBLE
//                        }
//                    }
//                }
//            }
//        }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SliderCardItemBinding.inflate(layoutInflater, parent, false)
//                val binding = SliderCardItem2Binding.inflate(layoutInflater, parent, false)
                return CardViewHolder(binding)
            }

        }
    }

}

class StarButtonListener(val clickListener: (card: Card) -> Unit) {
    fun onClick(card: Card) = clickListener(card)
}

class AudioBtnListener(val clickListener: (s: String) -> Unit) {
    fun onClick(s: String) = clickListener(s)
}










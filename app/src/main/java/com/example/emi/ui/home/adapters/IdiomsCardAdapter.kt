package com.example.emi.ui.home.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.database.Card
import com.example.emi.databinding.IdiomsCardItemHomeBinding


class IdiomsCardAdapter
    : ListAdapter<Card, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem === newItem
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
        (holder as CardViewHolder).bind(card)
    }

    class CardViewHolder private constructor( val binding: IdiomsCardItemHomeBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Card) {
            item.apply {
                binding.engWord.text = engWord
                binding.rusWord.text = rusWord
            }
        }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IdiomsCardItemHomeBinding.inflate(layoutInflater, parent, false)
                return CardViewHolder(binding)
            }
        }
    }
}

//private class CardsComparator1() : DiffUtil.ItemCallback<Card>() {
//    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
//        return oldItem === newItem
//    }
//
//    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
//        return oldItem == newItem
//    }
//}
package com.example.emi.ui.home.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.database.Card
import com.example.emi.databinding.IdiomsCardItemHomeBinding


class IdiomsCardAdapter(val clickListener: IdiomsCardListener)
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
//        val card = getItem(position)
//        (holder as CardViewHolder).bind(card)
        (holder as CardViewHolder).bind(getItem(position)!!, clickListener)
    }

    class CardViewHolder private constructor(private val binding: IdiomsCardItemHomeBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Card, clickListener: IdiomsCardListener) {
            binding.card = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
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

class IdiomsCardListener(val clickListener: (cardId: Long) -> Unit) {
    fun onClick(card: Card) = clickListener(card.id)
}
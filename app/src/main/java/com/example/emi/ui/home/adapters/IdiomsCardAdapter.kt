package com.example.emi.ui.home.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.R
import com.example.emi.database.Card


class IdiomsCardAdapter
    : ListAdapter<Card, RecyclerView.ViewHolder>(CardsComparator1()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = getItem(position)
        (holder as CardViewHolder).bind(card)
    }

    class CardViewHolder private constructor(private val itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        val eng: TextView = itemView.findViewById(R.id.eng_idiom)
        val rus: TextView = itemView.findViewById(R.id.rus_idiom)


        fun bind(item: Card) {

            item.apply {
                eng.text = engWord
                rus.text = rusWord
            }
        }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.idioms_card_item_home, parent, false)
                return CardViewHolder(view)
            }
        }
    }
}

private class CardsComparator1() : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }
}
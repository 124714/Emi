package com.example.emi.ui.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.R
import com.example.emi.database.Card


class SliderAdapter
    : ListAdapter<Card, RecyclerView.ViewHolder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slider_card_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = getItem(position)
        (holder as MyViewHolder).bind(card)
    }

    class MyViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val namePerson: TextView = itemView.findViewById(R.id.name)
        val surnamePerson: TextView = itemView.findViewById(R.id.surname)

        fun bind(item: Card) {
            item.apply {
                namePerson.text = engWord
                surnamePerson.text = rusWord
            }
        }
    }
}

private class MyDiffCallback : DiffUtil.ItemCallback<Card>() {

    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }
}
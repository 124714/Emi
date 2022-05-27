package com.example.emi.ui.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewGroupCompat
import androidx.datastore.core.DataStore
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.MainActivity
import com.example.emi.R
import com.example.emi.animation.ViewAnimation
//import com.example.emi.data.SettingsDataStore
import com.example.emi.database.Card
import com.example.emi.databinding.ListCardItemRecycler2Binding
import com.example.emi.databinding.ListCardItemRecyclerBinding
import com.example.emi.databinding.SliderCardItemBinding
import com.example.emi.databinding.TestLayoutBinding
import com.example.emi.ui.home.adapters.IdiomsCardAdapter
import timber.log.Timber


class ListCardAdapter(private val clickListener: CardListener2)
    : ListAdapter<Card, RecyclerView.ViewHolder>(ListDiffCallBack) {


    companion object ListDiffCallBack : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return ListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = getItem(position)
        (holder as ListViewHolder).bind(card, clickListener, position)
    }

    class ListViewHolder
    private constructor(private val binding: ListCardItemRecycler2Binding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Card, clickListener: CardListener2, position: Int) {

            with(binding) {
                card = item
                cardAndPosition = CardAndPosition(item, position)
                this.clickListener2 = clickListener
                executePendingBindings()
            }
        }

        companion object {
            fun create(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ListCardItemRecyclerBinding.inflate(layoutInflater, parent, false)
                val binding = ListCardItemRecycler2Binding.inflate(layoutInflater, parent, false)
                return ListViewHolder(binding)
            }
        }
    }
}

class CardListener(val clickListener: (cardId: Long) -> Unit) {
    fun onClick(cardId: Long) = clickListener(cardId)
}

class CardListener2(val clickListener: (cardId: Long, position: Int) -> Unit) {
    fun onClick(cardId: Long, position: Int) = clickListener(cardId, position)
}

class CardAndPosition(val card: Card, val position: Int)


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
import com.example.emi.data.SettingsDataStore
import com.example.emi.database.Card
import com.example.emi.databinding.ListCardItemRecyclerBinding
import com.example.emi.databinding.SliderCardItemBinding
import com.example.emi.databinding.TestLayoutBinding
import com.example.emi.ui.home.adapters.IdiomsCardAdapter
import timber.log.Timber


class ListCardAdapter
    : ListAdapter<Card, RecyclerView.ViewHolder>(ListDiffCallBack) {


    companion object ListDiffCallBack : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem === newItem
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
        (holder as ListViewHolder).bind(card)
    }

    class ListViewHolder
    private constructor(private val binding: ListCardItemRecyclerBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Card) {
            item.apply {
                binding.engWord.text = rusWord
                binding.rusWord.text = rusWord
                binding.executePendingBindings()
            }
        }

        companion object {
            fun create(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListCardItemRecyclerBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(binding)
            }
        }
    }
}


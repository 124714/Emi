package com.example.emi.ui.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.R
import com.example.emi.animation.ViewAnimation
import com.example.emi.database.Card
import com.example.emi.database.TestData
import com.example.emi.databinding.SliderCardItemBinding
import timber.log.Timber


class SliderAdapter(private val viewModel: SliderViewModel,
                    val starClickListener: StarButtonListener )
    : ListAdapter<Card, RecyclerView.ViewHolder>(SliderDiffCallBack) {


    companion object SliderDiffCallBack : DiffUtil.ItemCallback<Card>() {
        // изменилось ли содержимое элемента
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {

            val a = oldItem.id == newItem.id
            Timber.i("areItemsTheSame: $a")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            Timber.i("cardId: ${oldItem.id} oldItemMark: ${oldItem.mark} newItemMark: ${newItem.mark}")
//            return oldItem == newItem
            val a = oldItem.mark == newItem.mark
            Timber.i("areContentensTheSame: $a")
            return oldItem == newItem
        }
    }

    override fun onCurrentListChanged(previousList: MutableList<Card>, currentList: MutableList<Card>) {
        super.onCurrentListChanged(previousList, currentList)
        Timber.i("list changed")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return CardViewHolder.create(parent, viewModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = getItem(position)
        (holder as CardViewHolder).bind(card, viewModel, starClickListener)

    }

    class CardViewHolder private constructor(private val binding: SliderCardItemBinding, private val viewModel: SliderViewModel)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


        fun bind(item: Card, viewModel: SliderViewModel, starClickListener: StarButtonListener) {
            item.apply {
                binding.card = item
                binding.starClickListener = starClickListener
                Timber.i("BIND[${item.id}]: ${item.mark}")
                binding.executePendingBindings()
            }
        }
        init {
//                setupFlipCardListener()
//                setupBtnStarListener()
        }

//        fun setupBtnStarListener(card: Card, viewModel: SliderViewModel){
//            binding.btnStar.setOnClickListener{
//
//
//                viewModel.addCard()
//            }
//
//            binding.btnStar.setOnClickListener {
//                Timber.i("hello")
//                justAddedCard.add(binding.card!!)
//                // Исправить!!! - очистить livedata
//                sendToViewModel(binding.card!!)
//                binding.card?.mark = !binding.card!!.mark
//                when (binding.card?.mark) {
//                    true -> {
//                        binding.btnStar.setImageResource(R.drawable.star)
//                        viewModel.setMark(binding.card!!)
//                    }
//                    false -> {
//                        binding.btnStar.setImageResource(R.drawable.star_filled)
//                        viewModel.unmark(binding.card!!)
//                    }
//                }
//                ViewAnimation.scaler(binding.btnStar, binding.btnStar)
//                when(binding.card?.mark) {
//                    true -> {
//                        binding.btnStar.setImageResource(R.drawable.star_filled)
//                    }
//                    false -> binding.btnStar.setImageResource(R.drawable.star)
//                }
//            }
//        }

//        private fun setupFlipCardListener() {
//            binding.flipCard.setOnClickListener {
////                Timber.i("hello")
//                ViewAnimation.rotater(binding.flippedCard, binding.flipCard)
//                binding.engWord.text =
//                    when (binding.engWord.text) {
//                        binding.card?.engWord -> binding.card?.rusWord
//                        else -> binding.card?.engWord
//                    }
//            }
//        }

        // отправить только что добавленные карточки в SliderViewModel
//        private fun sendToViewModel(card: Card) {
//            when(card.mark) {
//                false -> viewModel.addCardFromSlider(justAddedCard)
//                true -> {
//                    if(card in justAddedCard) {
//                        justAddedCard.remove(card)
//                        viewModel.addCardFromSlider(justAddedCard)
//                    }
//                }
//            }
//
//        }

        companion object {
            fun create(parent: ViewGroup, viewModel: SliderViewModel): CardViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SliderCardItemBinding.inflate(layoutInflater, parent, false)

                return CardViewHolder(binding, viewModel)
            }
            // Только что добавленные карточки
            private val justAddedCard: MutableList<Card> = mutableListOf()


        }

        override fun onClick(p0: View?) {
            Timber.i("onClick")
            when(p0?.id) {
                binding.btnStar.id -> binding.btnStar.setImageResource(R.drawable.star)

            }
        }
    }

}

class StarButtonListener(val starClickListener: (card: Card) -> Unit) {
    fun onStarClick(card: Card) = starClickListener(card)
}







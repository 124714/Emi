package com.example.emi.ui.home.adapters

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EdgeEffect
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.R
import com.example.emi.database.Card
import com.example.emi.database.DateAndAmountCards
import com.example.emi.database.Progress
import com.example.emi.databinding.ListCardItemHome2Binding
import com.example.emi.databinding.ListCardItemHomeBinding
import com.example.emi.dayPassed
import timber.log.Timber


class LearnedCardAdapter(private val clickListener: OnClickListener)
    : ListAdapter<DateAndAmountCards, RecyclerView.ViewHolder>(DiffCallback) {

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            recyclerView.forEachVisibleHolder { holder: CardViewHolder ->
                holder.rotation
                    // Update the velocity.
                    // The velocity is calculated by the horizontal scroll offset.
                    .setStartVelocity(holder.currentVelocity - dx * SCROLL_ROTATION_MAGNITUDE)
                    // Start the animation. This does nothing if the animation is already running.
                    .start()
            }
        }
    }

    val edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect {
            return object : EdgeEffect(recyclerView.context) {

                override fun onPull(deltaDistance: Float) {
                    super.onPull(deltaDistance)
                    handlePull(deltaDistance)
                }

                override fun onPull(deltaDistance: Float, displacement: Float) {
                    super.onPull(deltaDistance, displacement)
                    handlePull(deltaDistance)
                }

                private fun handlePull(deltaDistance: Float) {
                    // This is called on every touch event while the list is scrolled with a finger.
                    // We simply update the view properties without animation.
                    val sign = if (direction == DIRECTION_RIGHT) -1 else 1
                    val rotationDelta = sign * deltaDistance * OVERSCROLL_ROTATION_MAGNITUDE
                    val translationXDelta =
                        sign * recyclerView.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                    recyclerView.forEachVisibleHolder { holder: CardViewHolder ->
                        holder.rotation.cancel()
                        holder.translationX.cancel()
                        holder.itemView.rotation += rotationDelta
                        holder.itemView.translationX += translationXDelta
                    }
                }

                override fun onRelease() {
                    super.onRelease()
                    // The finger is lifted. This is when we should start the animations to bring
                    // the view property values back to their resting states.
                    recyclerView.forEachVisibleHolder { holder: CardViewHolder ->
                        holder.rotation.start()
                        holder.translationX.start()
                    }
                }

                override fun onAbsorb(velocity: Int) {
                    super.onAbsorb(velocity)
                    val sign = if (direction == DIRECTION_RIGHT) -1 else 1
                    // The list has reached the edge on fling.
                    val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                    recyclerView.forEachVisibleHolder { holder: CardViewHolder ->
                        holder.translationX
                            .setStartVelocity(translationVelocity)
                            .start()
                    }
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DateAndAmountCards>() {
        override fun areItemsTheSame(oldItem: DateAndAmountCards, newItem: DateAndAmountCards): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: DateAndAmountCards, newItem: DateAndAmountCards): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {

        return CardViewHolder.create(parent).also {
            // The rotation pivot should be at the center of the top edge.
            it.itemView.doOnLayout { v -> v.pivotX = v.width / 2f }
            it.itemView.pivotY = 0f
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = getItem(position)
        (holder as CardViewHolder).bind(card, clickListener)
    }

    class CardViewHolder private constructor(val binding: ListCardItemHome2Binding)
        : RecyclerView.ViewHolder(binding.root) {

        var currentVelocity = 0f
        val rotation: SpringAnimation = SpringAnimation(itemView, SpringAnimation.ROTATION)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
            .addUpdateListener { _, _, velocity ->
                currentVelocity = velocity
            }

        val translationX: SpringAnimation = SpringAnimation(itemView, SpringAnimation.TRANSLATION_X)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )

        fun bind(item: DateAndAmountCards, clickListener: OnClickListener) {
            with(binding) {
                cardsAmount2.text = item.amount.toString()
                checkBox.setOnClickListener{ view ->
                    clickListener.onClick(item.date!!, (view as CheckBox).isChecked)
                }

                dayPassed2.text = item.date/*dayPassed(item.date!!)*/
                executePendingBindings()
                }
            }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ListCardItemHomeBinding.inflate(layoutInflater, parent, false)
                val binding = ListCardItemHome2Binding.inflate(layoutInflater, parent, false)
                return CardViewHolder(binding)
            }
        }
    }

    interface OnClickListener {
        fun onClick(date: String, isChecked: Boolean)
    }
}


class LearnedCardListener(val clickListener: (date: DateChecked) -> Unit) {
    fun onClick(date: DateChecked) = clickListener(date)
}

private inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
    action: (T) -> Unit
) {
    for (i in 0 until childCount) {
        action(getChildViewHolder(getChildAt(i)) as T)
    }
}

/** The magnitude of rotation while the list is scrolled. */
private const val SCROLL_ROTATION_MAGNITUDE = 0.25f
/** The magnitude of rotation while the list is over-scrolled. */
private const val OVERSCROLL_ROTATION_MAGNITUDE = -10
/** The magnitude of translation distance while the list is over-scrolled. */
private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.2f
/** The magnitude of translation distance when the list reaches the edge on fling. */
private const val FLING_TRANSLATION_MAGNITUDE = 0.5f
data class DateChecked(
    val date: Long?,
    var checked: Boolean = false
)

//fun onCheckboxClicked(view: View) {
//            if (view is CheckBox) {
//                val checked: Boolean = view.isChecked
//
//                when (view.id) {
//                    R.id.mark_progress -> {
//                        if (checked) {
//
//                        } else {
//
//                        }
//                    }
//
//                }
//            }
//        }
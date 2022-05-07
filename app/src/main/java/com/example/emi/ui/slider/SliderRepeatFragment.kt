package com.example.emi.ui.slider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.emi.CardsApplication
import com.example.emi.R
//import com.example.emi.data.SettingsDataStore
import com.example.emi.data.UserPreferencesRepository
import com.example.emi.data.dataStore
import com.example.emi.database.Card
import com.example.emi.databinding.FragmentSliderBinding
import com.example.emi.databinding.FragmentSliderRepeatBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip

class SliderRepeatFragment : Fragment() {
    private val sliderViewModel: SliderViewModel by activityViewModels() {
        SliderViewModelFactory(
            (requireNotNull(this.activity).application as CardsApplication).repository,
        UserPreferencesRepository(requireContext().dataStore)
        )
    }
    private var _binding: FragmentSliderRepeatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: SliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSliderRepeatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        observeJustAddedCard()
        observeChips()

        binding.fab.setOnClickListener{
            findNavController().navigate(SliderRepeatFragmentDirections.actionSliderRepeatFragmentToNavigationSlider())
        }
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        adapter = SliderAdapter(
            StarButtonListener { card ->
                sliderViewModel.updateCard(card)
            }
        )
        viewPager.adapter = adapter
    }

    private fun observeChips() {
        sliderViewModel.cardList.observe(viewLifecycleOwner, object: Observer<MutableList<Card>> {
            override fun onChanged(data: MutableList<Card>?) {
                data ?: return
                // Создаем новый Chip для каждого элемента в списке
                val chipGroup = binding.wordList
                val inflator = LayoutInflater.from(chipGroup.context)

                val children = data.map {card ->
                    val chip = inflator.inflate(R.layout.cards_visible_chips, chipGroup, false) as Chip
                    chip.text = card.engWord
                    chip.tag = card
                    chip.setOnCheckedChangeListener { button, isChecked ->
                        sliderViewModel.onChangeFilter(button.tag as Card, isChecked)
                    }
                    chip
                }

                chipGroup.removeAllViews()

                for (chip in children) {
                    chipGroup.addView(chip)
                }

            }
        })
    }
    private fun observeJustAddedCard(){
        sliderViewModel.justAddedCard.observe(this) { cards ->
            adapter.submitList(cards)
        }
    }


}
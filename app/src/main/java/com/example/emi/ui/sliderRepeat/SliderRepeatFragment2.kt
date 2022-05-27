package com.example.emi.ui.sliderRepeat

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.emi.CardsApplication
import com.example.emi.R
//import com.example.emi.data.SettingsDataStore
import com.example.emi.data.UserPreferencesRepository
import com.example.emi.data.dataStore
import com.example.emi.databinding.FragmentSliderRepeatBinding
import com.example.emi.ui.slider.*
import com.google.android.material.chip.Chip

class SliderRepeatFragment2 : Fragment() {
    private val sliderRepeatViewModel: SliderRepeatViewModel by activityViewModels() {
        SliderRepeatViewModelFactory(
            (requireNotNull(this.activity).application as CardsApplication).repository,
            UserPreferencesRepository(requireContext().dataStore)
        )
    }
    private var _binding: FragmentSliderRepeatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: SliderAdapter



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSliderRepeatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root


    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = SliderRepeatFragment2Args.fromBundle(requireArguments())
        sliderRepeatViewModel.addCardsForDate(*args.dateForRepeat)
        setupViewPager()
        observeJustAddedCard()
        observeChips()
//        binding.fab.setOnClickListener {
//
//        }
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        adapter = SliderAdapter(
            StarButtonListener { card ->
//                sliderRepeatViewModel.updateCard(card)
            },
            AudioBtnListener { card ->
                "Hello"
            }
        )
        viewPager.adapter = adapter
    }
    private fun observeChips() {
        sliderRepeatViewModel.listWord.observe(viewLifecycleOwner, object: Observer<MutableList<String>> {
            override fun onChanged(data: MutableList<String>?) {
                data ?: return
                // Создаем новый Chip для каждого элемента в списке
                val chipGroup = binding.wordList
                val inflator = LayoutInflater.from(chipGroup.context)
                val children = data.map {card ->
                    val chip = inflator.inflate(R.layout.cards_visible_chips, chipGroup, false) as Chip
                    chip.text = card
                    chip.tag = card
                    chip.setOnCheckedChangeListener { button, isChecked ->
                        sliderRepeatViewModel.onChangeFilter(button.tag as String, isChecked)
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

    private fun observeJustAddedCard() {
        sliderRepeatViewModel.cardAndDate.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards)
            }
        }



}
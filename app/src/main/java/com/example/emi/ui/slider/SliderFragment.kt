package com.example.emi.ui.slider


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.emi.CardsApplication
import com.example.emi.R
import com.example.emi.convertLongToDateString
import com.example.emi.data.SettingsDataStore
import com.example.emi.data.dataStore

import com.example.emi.databinding.FragmentSliderBinding
import com.google.android.material.slider.Slider

import kotlinx.coroutines.launch

import timber.log.Timber

class SliderFragment : Fragment() {
    private val sliderViewModel: SliderViewModel by viewModels {
        SliderViewModelFactory((requireNotNull(this.activity).application as CardsApplication).repository)
    }
    private var _binding: FragmentSliderBinding? = null
    private val binding get() = _binding!!
    private lateinit var positionDataStore: SettingsDataStore
    private lateinit var viewPager: ViewPager2
    private var startPositionViewPager = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSliderBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.sliderViewModel = sliderViewModel
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.viewPager
        positionDataStore = SettingsDataStore(requireContext().dataStore)
        val adapter = SliderAdapter(
            StarButtonListener { card ->
                sliderViewModel.updateCard(card.copy().apply{mark = !mark})
            }
            )

        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                startPositionViewPager = position
                lifecycleScope.launch{
                    positionDataStore.saveLayoutToPreferencesStore(position, requireContext())
                }
            }
        })


        //---------------------------------------------------------------------------------
        positionDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            startPositionViewPager = value
            sliderViewModel.allCards.observe(this) { words ->
                words.let {
                    adapter.submitList(words)
                    viewPager.currentItem = startPositionViewPager
                }

            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("destroyed")
//        viewPager.unregisterOnPageChangeCallback()
        _binding = null
    }

}
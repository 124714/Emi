package com.example.emi.ui.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.emi.CardsApplication
import com.example.emi.databinding.FragmentSliderBinding

class SliderFragment : Fragment() {

    private val sliderViewModel: SliderViewModel by viewModels {
        SliderViewModelFactory((requireNotNull(this.activity).application as CardsApplication).repository)
    }
    private var _binding: FragmentSliderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        sliderViewModel =
//            ViewModelProvider(this).get(SliderViewModel::class.java)

        _binding = FragmentSliderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewPager: ViewPager2 = binding.viewPager

        val listPersons =
            listOf<Person>(
                Person("andrey", "petrov"),
                Person("maria", "smirnova"),
                Person("Tony", "Stark")
            )

        val adapter = SliderAdapter()
        viewPager.adapter = adapter

        sliderViewModel.allCards.observe(this) {words ->
            words.let {
                adapter.submitList(words)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
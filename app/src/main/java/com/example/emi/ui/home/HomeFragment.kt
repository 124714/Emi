package com.example.emi.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.emi.CardsApplication
import com.example.emi.R
import com.example.emi.databinding.FragmentHomeBinding
import com.example.emi.databinding.ListCardItemHomeVp2Binding
import com.example.emi.ui.home.adapters.IdiomsCardAdapter
import com.example.emi.ui.home.adapters.IdiomsCardListener
import com.example.emi.ui.home.adapters.LearnedCardAdapter
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireNotNull(this.activity).application as CardsApplication).repository)
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val listCardItemHome = binding.listCardItemHome
        val adapter = LearnedCardAdapter()
        listCardItemHome.adapter = adapter

        homeViewModel.allCards.observe(this) {words ->
            words.let {
                adapter.submitList(words)
            }
        }

        // ---------------------------------------------------------------

        val idiomsCardItem = binding.idiomsCardItem
        val adapter2 = IdiomsCardAdapter(IdiomsCardListener {
            id -> Toast.makeText(context, "card is pressed [$id]", Toast.LENGTH_SHORT ).show()
        })
        idiomsCardItem.adapter = adapter2

        homeViewModel.allIdioms.observe(this) {words ->
            words.let {
                adapter2.submitList(words)
            }
        }

        // -----------------------------------------------------

        homeViewModel.category.observe(viewLifecycleOwner, object: Observer<List<String>> {
            override fun onChanged(data: List<String>?) {
                data ?: return
                // Создаем новый Chip для каждого элемента в списке
                val chipGroup = binding.regionList
                val inflator = LayoutInflater.from(chipGroup.context)

                val children = data.map {category ->
                    val chip = inflator.inflate(R.layout.region_learned_card_chips, chipGroup, false) as Chip
                    chip.text = category
                    chip.tag = category
                    chip.setOnCheckedChangeListener{ button, isChecked ->
                        Toast.makeText(context, "is pressed", Toast.LENGTH_SHORT).show()
                    }
                    chip
                }

                chipGroup.removeAllViews()

                for (chip in children) {
                    chipGroup.addView(chip)
                }

            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
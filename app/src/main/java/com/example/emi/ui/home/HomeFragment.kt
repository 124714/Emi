package com.example.emi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.emi.CardsApplication
import com.example.emi.databinding.FragmentHomeBinding
import com.example.emi.ui.home.adapters.LearnedCardAdapter

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
    ): View? {


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





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
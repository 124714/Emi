package com.example.emi.ui.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.emi.CardsApplication
import com.example.emi.R
import com.example.emi.animation.Stagger
import com.example.emi.databinding.FragmentHomeBinding
import com.example.emi.databinding.ListFragmentBinding
import com.example.emi.ui.home.HomeViewModel
import com.example.emi.ui.home.HomeViewModelFactory

class ListFragment : Fragment() {
    private val listViewModel: ListViewModel by viewModels {
        ListViewModelFactory((requireNotNull(this.activity).application as CardsApplication).repository)
    }
    private var _binding: ListFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val adapter = ListCardAdapter()
        binding.list.adapter = adapter
//
        // We animate item additions on our side, so disable it in RecyclerView.
        binding.list.itemAnimator = object : DefaultItemAnimator() {
            override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
                dispatchAddFinished(holder)
                dispatchAddStarting(holder)
                return false
            }
        }
//
//        // This is the transition for the stagger effect.
        val stagger = Stagger()

        listViewModel.allCards.observe(this) { cheeses ->
            // Delay the stagger effect until the list is updated.
            TransitionManager.beginDelayedTransition(binding.list, stagger)
            adapter.submitList(cheeses)
        }
    }


}
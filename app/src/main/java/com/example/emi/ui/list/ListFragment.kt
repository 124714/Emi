package com.example.emi.ui.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
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
import timber.log.Timber
import java.util.Observer

class ListFragment : Fragment() {
    private val listViewModel: ListViewModel by viewModels {
        ListViewModelFactory((requireNotNull(this.activity).application as CardsApplication).repository)
    }
    private var _binding: ListFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: ListCardAdapter
    private val stagger = Stagger()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.lifecycleOwner = viewLifecycleOwner
        Timber.i("111111111111111111111")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupCardsList()
        observeCardsList()
        observeNavigateToSliderFragment()

    }

    private fun setupCardsList() {
        adapter = ListCardAdapter(CardListener2 { cardId, pos ->
            Toast.makeText(context, "id: $cardId, position: $pos", Toast.LENGTH_SHORT).show()
            listViewModel.onListItemClicked(pos)
        })
        binding.list.adapter = adapter
        binding.list.itemAnimator = object : DefaultItemAnimator() {
            override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
                dispatchAddFinished(holder)
                dispatchAddStarting(holder)
                return false
            }
        }

    }
    private fun observeCardsList() {
        listViewModel.allCards.observe(viewLifecycleOwner) { cheeses ->
            // Delay the stagger effect until the list is updated.
            TransitionManager.beginDelayedTransition(binding.list, stagger)
            adapter.submitList(cheeses)
        }
    }

    private fun observeNavigateToSliderFragment() {
        listViewModel.navigateToSliderFragment.observe(viewLifecycleOwner) { pos ->
            pos?.let {
                findNavController().navigate(ListFragmentDirections.actionNavigationListToNavigationSlider(pos))
                listViewModel.doneNavigating()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
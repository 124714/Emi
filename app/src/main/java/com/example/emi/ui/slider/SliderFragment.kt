package com.example.emi.ui.slider

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.example.emi.CardsApplication
import com.example.emi.data.*
import com.example.emi.databinding.FragmentSliderBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber



class SliderFragment : Fragment() {
    private val sliderViewModel: SliderViewModel by activityViewModels() {
        SliderViewModelFactory(
            (requireNotNull(this.activity).application as CardsApplication).repository,
            UserPreferencesRepository(requireContext().dataStore))
    }

    private var _binding: FragmentSliderBinding? = null
    private val binding get() = _binding!!
    private lateinit var list: RecyclerView
    private lateinit var adapter: SliderAdapter
    private val snapHelper = PagerSnapHelper()
    private lateinit var layoutManager: LinearLayoutManager
    private var isScrollUpdate = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSliderBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()

        sliderViewModel.initialSetupEvent.observe(viewLifecycleOwner) {
            updatePosition(it.startPositionViewPager)
            observePreferenceChanges()
        }

        binding.fab.setOnClickListener{
            findNavController().navigate(SliderFragmentDirections.actionNavigationSliderToSliderRepeatFragment())
        }

    }

    private fun setupViewPager() {
        list = binding.list
        adapter = SliderAdapter(
            StarButtonListener { card ->
                sliderViewModel.updateCard(card)
                }
        )
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list.layoutManager = layoutManager
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                updatePosition(layoutManager.findFirstVisibleItemPosition())
            }
        })
        list.adapter = adapter
        snapHelper.attachToRecyclerView(list)
    }

    private fun observePreferenceChanges() {
        sliderViewModel.cardListModel.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards.cards)
            if (isScrollUpdate) {
                updateScrollPosition(cards.startPosition)
                isScrollUpdate = false
            }
        }
    }

    private fun updatePosition(pos: Int) {
        sliderViewModel.savePosition(pos)
    }

    private fun updateScrollPosition(pos: Int) {
        list.scrollToPosition(pos)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStop() {
        super.onStop()

        sliderViewModel.saveLastPosition()
    }

}
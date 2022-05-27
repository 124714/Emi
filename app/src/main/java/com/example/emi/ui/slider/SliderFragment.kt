package com.example.emi.ui.slider

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.emi.CardsApplication
import com.example.emi.R
import com.example.emi.data.*
import com.example.emi.database.Progress
import com.example.emi.databinding.FragmentSliderBinding
import com.example.emi.databinding.ListFragmentBinding
import com.example.emi.ui.sliderRepeat.SliderRepeatFragment2Args
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

    private val args by navArgs<SliderFragmentArgs>()
    private val position by lazy { args.position}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView")
        _binding = FragmentSliderBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
//        val args = SliderFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "${args.position}", Toast.LENGTH_SHORT).show()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        Timber.i("onViewCreated")
        sliderViewModel.initialSetupEvent.observe(viewLifecycleOwner) {
            updatePosition(it.startPositionViewPager)
            observePreferenceChanges()
        }
//        sliderViewModel.allProgress.observe(viewLifecycleOwner) {progress ->
//            Timber.i("$progress")
//        }
        binding.fab.setOnClickListener{
            findNavController().navigate(SliderFragmentDirections.actionNavigationSliderToSliderRepeatFragment())

        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
    }

    private fun setupViewPager() {
        list = binding.list
        adapter = SliderAdapter(
            StarButtonListener { card ->
                sliderViewModel.updateCard(card)
                sliderViewModel.updateFilter(card.copy().apply { mark = !mark })
                sliderViewModel.updateProgress(Progress(0, card.id), !card.mark)
                },
            AudioBtnListener {word ->

                // переделать (MediaPlayer.OnPrepareListener)
                MediaPlayer
                    .create(context, resources
                        .getIdentifier(convertToTextResourseName(word), "raw", context?.packageName))
                    .start()
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
            if (sliderViewModel.isScrollUpdate) {
                updateScrollPosition(cards.startPosition)
                sliderViewModel.isScrollUpdate = false
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

    private fun convertToTextResourseName(s: String): String {

        val apostrophe = "'"
        val space = " "
        return if (apostrophe in s) {
            convertToTextResourseName(s.replace(apostrophe, ""))
        } else if(space in s) {
            convertToTextResourseName(s.replace(space, "_"))
        } else {
            s
        }

    }
}
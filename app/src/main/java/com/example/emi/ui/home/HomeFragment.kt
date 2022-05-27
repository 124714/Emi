package com.example.emi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.emi.*
import com.example.emi.animation.Stagger
import com.example.emi.databinding.FragmentHomeBinding
import com.example.emi.ui.home.adapters.IdiomsCardAdapter
import com.example.emi.ui.home.adapters.IdiomsCardListener
import com.example.emi.ui.home.adapters.LearnedCardAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextInt

class HomeFragment : Fragment(), LearnedCardAdapter.OnClickListener {
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireNotNull(this.activity).application as CardsApplication).repository)
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var cardDeckForRepeatList: RecyclerView
    private lateinit var adapter: LearnedCardAdapter
    private lateinit var snapHelper: LinearSnapHelper

    private var currentState: Int = BottomSheetBehavior.STATE_EXPANDED


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // ---------------------------------------------------------------

        val idiomsCardItem = binding.idiomsCardItem
        val adapter2 = IdiomsCardAdapter(IdiomsCardListener { id ->
            Toast.makeText(context, "card is pressed [$id]", Toast.LENGTH_SHORT).show()
        })
        idiomsCardItem.adapter = adapter2

        homeViewModel.allIdioms.observe(viewLifecycleOwner) { words ->
            words.let {
                adapter2.submitList(words)
            }
        }

        setFragmentResultListener("requestKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getString("bundleKey")
            // Do something with the result
            binding.periodChip.text = result
            homeViewModel.onFilterChangeForPeriod(result!!)
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupCardDeckForRepeate()
        observeCardDeck()
        observeSelectedCard()
        observeBtnShowEvent()

        setupOnCheckedChangeListeners()
        //----------------------------------------

        binding.periodChip.setOnCheckedChangeListener { view, isChecked ->
//            homeViewModel.onFilterChanged("2021-01-12", "2021-01-12")
            homeViewModel.updateFilterForPeriod (tag = HomeViewModel.CARDS_FOR_PERIOD, isChecked )
            when(isChecked) {
                true -> BackDropFragment().show(parentFragmentManager, "ModalBottomSheet")
                false -> view.text = "период"
            }
        }
        //----------------------------------------

    }


    private fun observeCardDeck() {
        homeViewModel.amountCardsAndDates.observe(viewLifecycleOwner) { words ->
                adapter.submitList(words)

        }
    }

    private fun observeBtnShowEvent() {
        homeViewModel.btnShowEvent.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(HomeFragmentDirections
                            .actionNavigationHomeToSliderRepeatFragment2(homeViewModel.getDatesForRepeat())
                    )
                homeViewModel.dates.clear()
                homeViewModel.doneNavigating()
            }
        }
    }

    private fun observeSelectedCard() {
        homeViewModel.selectedCardsForDate.observe(viewLifecycleOwner) { dates ->
            if (dates.isNotEmpty() && dates.values.reduce { acc, b -> acc || b })
                binding.btnShow.visibility = View.VISIBLE
            else {
                binding.btnShow.visibility = View.GONE
            }
        }
    }

    private fun setupCardDeckForRepeate() {
        snapHelper = LinearSnapHelper()
        cardDeckForRepeatList = binding.listCardItemHome
        adapter = LearnedCardAdapter(this)

        cardDeckForRepeatList.adapter = adapter

        cardDeckForRepeatList.addOnScrollListener(adapter.onScrollListener)
        cardDeckForRepeatList.edgeEffectFactory = adapter.edgeEffectFactory
        snapHelper.attachToRecyclerView(cardDeckForRepeatList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(date: String, isChecked: Boolean) {
        Toast.makeText(context, "$date: $isChecked", Toast.LENGTH_SHORT).show()
        homeViewModel.dates[date] = isChecked
        homeViewModel.setDates(date, isChecked)
    }

//


    private fun setupOnCheckedChangeListeners() {

        binding.threeDayChip.setOnCheckedChangeListener { view, isChecked ->
            homeViewModel.onFilterChanged(tag = HomeViewModel.CARDS_FOR_THREE_DAY, isChecked = isChecked)
            when(isChecked) {
                true -> view.text = homeViewModel.getFilterStatus()
                false -> view.text = "три дня"
            }
        }
        binding.weekChip.setOnCheckedChangeListener { view, isChecked ->
            homeViewModel.onFilterChanged(tag = HomeViewModel.CARDS_FOR_WEEK, isChecked = isChecked)
            when(isChecked) {
                true -> view.text = homeViewModel.getFilterStatus()
                false -> view.text = "неделя"
            }
        }
        binding.monthChip.setOnCheckedChangeListener { view, isChecked ->
            homeViewModel.onFilterChanged(tag = HomeViewModel.CARDS_FOR_MONTH, isChecked = isChecked)
            when(isChecked) {
                true -> view.text = homeViewModel.getFilterStatus()
                false -> view.text = "месяц"
            }
        }
//        binding.periodChip.setOnCheckedChangeListener { view, isChecked ->
//            when (isChecked) {
//                true -> homeViewModel.onFilterChanged(tag = HomeViewModel.CARDS_FOR_PERIOD, isChecked = isChecked)
//                false -> homeViewModel.onFilterChanged(tag = HomeViewModel.ALL_CARDS, isChecked = isChecked)
//            }
//        }
    }


    //-----------------------------------------------------------------------------


}

//        (activity as MainActivity).setOnBottomSheetCallbacks(this)

//    private fun observeChips() {
//        homeViewModel.category.observe(viewLifecycleOwner, object: Observer<List<String>> {
//            override fun onChanged(data: List<String>?) {
//                data ?: return
//                // Создаем новый Chip для каждого элемента в списке
//                val chipGroup = binding.regionList
//                val inflator = LayoutInflater.from(chipGroup.context)
//
//                val children = data.map {category ->
//                    val chip = inflator.inflate(R.layout.region_learned_card_chips, chipGroup, false) as Chip
//                    chip.text = category
//                    chip.tag = category
//                    chip.setOnCheckedChangeListener{ button, isChecked ->
//                        Toast.makeText(context, "is pressed", Toast.LENGTH_SHORT).show()
//                        (activity as MainActivity).openBottomSheet()
//                    }
//                    chip
//                }
//
//                chipGroup.removeAllViews()
//
//                for (chip in children) {
//                    chipGroup.addView(chip)
//                }
//
//            }
//        })
//    }


//        binding.btnDatePicker.setOnClickListener{
//            val dateRangePicker =
//                MaterialDatePicker.Builder.dateRangePicker()
//                    .setTitleText("Select dates")
//                    .setSelection(
//                        Pair(
//                            MaterialDatePicker.thisMonthInUtcMilliseconds(),
//                            MaterialDatePicker.todayInUtcMilliseconds()
//                        )
//                    )
//                    .build()
//            dateRangePicker.addOnPositiveButtonClickListener {
//                Toast.makeText(context, "${convertLongToDateString(it.first)} - ${
//                    com.example.emi.convertLongToDateString(it.second)}", Toast.LENGTH_LONG).show()
//                    homeViewModel.onFilterChanged(convertLongToDateString(it.first), convertLongToDateString(it.second))
//
//            }
//            dateRangePicker.show(childFragmentManager, "tag")
//
//        }


//--------------------------------------------------------------------
//        binding.btnDatePicker.setOnClickListener(NavigationIconClickListener(
//            activity!!,
//            view.product_grid,
//            AccelerateDecelerateInterpolator(),
//            ContextCompat.getDrawable(context!!, R.drawable.shr_branded_menu), // Menu open icon
//            ContextCompat.getDrawable(context!!, R.drawable.shr_close_menu))) // Menu close icon
//--------------------------------------------------------------------

//override fun onStateChanged(bottomSheet: View, newState: Int) {
//        currentState = newState
//        when (newState) {
//            BottomSheetBehavior.STATE_EXPANDED -> {
//
//            }
//            BottomSheetBehavior.STATE_COLLAPSED -> {
//
//            }
//        }
//    }

//        binding.btnDatePicker.setOnClickListener {
//            (activity as MainActivity).openBottomSheet()
//        }
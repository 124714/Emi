package com.example.emi.ui.home

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.emi.CardsApplication

import com.example.emi.R
import com.example.emi.databinding.FragmentBackDropBinding
import com.example.emi.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import java.text.SimpleDateFormat

private const val COLLAPSED_HEIGHT = 550
class BackDropFragment() : BottomSheetDialogFragment() {

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireNotNull(this.activity).application as CardsApplication).repository)
    }

    private var _binding: FragmentBackDropBinding? = null
    private val binding get() = _binding!!
    private var startDate: Boolean = false
    private var endDate: Boolean = false

    private lateinit var behavior: BottomSheetBehavior<View?>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentBackDropBinding.inflate(inflater, container, false)


        binding.startDateInput.onFocusChangeListener = OnFocusChangeListener { view, hasFocused ->
            if(hasFocused) {
                startDate = hasFocused
                Toast.makeText(context, "startDateInput is focused", Toast.LENGTH_SHORT).show()
            }else {
                startDate = hasFocused
                Toast.makeText(context, "startDateInput is not focused", Toast.LENGTH_SHORT).show()
            }
        }

        binding.endDateInput.onFocusChangeListener = OnFocusChangeListener { view, hasFocused ->
            if(hasFocused) {
                endDate = hasFocused
                Toast.makeText(context, "endDateInput is focused", Toast.LENGTH_SHORT).show()
            }else {
                endDate = hasFocused
                Toast.makeText(context, "endDateInput is not focused", Toast.LENGTH_SHORT).show()
            }
        }

        binding.calendarView.setOnDateChangeListener { p0, year, month, day ->
            if (startDate) {
                binding.startDateInput.text?.clear()
                binding.startDateInput.text?.insert(0, buildDateString(day, month + 1, year))
                val a = binding.startDateInput.text.toString()
//                Toast.makeText(context, a, Toast.LENGTH_SHORT).show()
//                validDate(binding.startDateInput.text.toString(), binding.endDateInput.text.toString())
//                validDate()

            } else if (endDate) {
                binding.endDateInput.text?.clear()
                binding.endDateInput.text?.insert(0, buildDateString(day, month + 1, year))
            }
        }

//        binding.btnOk.setOnClickListener{
//            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
////            homeViewModel.onFilterChanged("2021-01-12", "2021-01-12")
//            behavior.state = BottomSheetBehavior.STATE_HIDDEN
//
//        }

        binding.btnOk.setOnClickListener {
            val result = "${binding.startDateInput.text.toString()}:${binding.endDateInput.text.toString()}"
            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }


        binding.startDateInput.addTextChangedListener(TextFieldValidation(binding.startDateInput))
        binding.endDateInput.addTextChangedListener(TextFieldValidation(binding.startDateInput))

        return binding.root

    }

    fun validDate(): Boolean {
        val start = binding.startDateInput.text.toString().split("-").reversed().joinToString("-")
        val end = binding.endDateInput.text.toString().split("-").reversed().joinToString("-")

        if (start > end) {
            Toast.makeText(context, "$start , $end", Toast.LENGTH_SHORT).show()
            binding.textInputLayout2.error = INPUT_ERROR
            binding.textInputLayout2.requestFocus()
            return false
        }else {
            binding.textInputLayout2.isErrorEnabled = false
        }
       return true
    }

    private fun buildDateString(day: Int, month: Int, year: Int): String {
        val date = StringBuilder()
        when(day) {
            in 1..9 -> date.append("0$day-")
            else -> date.append("$day-")
        }
        when(month) {
            in 1..9 -> date.append("0$month-")
            else -> date.append("$month-")
        }
        date.append(year)
        return date.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        // Плотность понадобится нам в дальнейшем
        val density = requireContext().resources.displayMetrics.density
        dialog?.let {
            // Находим сам bottomSheet и достаём из него Behaviour
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            behavior = BottomSheetBehavior.from(bottomSheet)
            // Выставляем высоту для состояния collapsed и выставляем состояние collapsed
            behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }

    companion object {
        const val INPUT_ERROR = "дата начала периода больше, чем дата его конца"
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when(view.id) {
//                R.id.start_date_input -> Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
                R.id.start_date_input -> validDate()
//                R.id.end_date_input -> validDate()
            }
        }

    }



}

interface OnBottomSheetCallbacks {
    fun onStateChanged(s1: String, s2: String)
}
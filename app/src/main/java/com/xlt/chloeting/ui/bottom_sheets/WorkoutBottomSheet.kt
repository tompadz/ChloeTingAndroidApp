package com.xlt.chloeting.ui.bottom_sheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xlt.chloeting.R
import com.xlt.chloeting.databinding.SheetWorkoutBinding
import com.xlt.chloeting.util.FilterProgramType
import com.xlt.chloeting.util.listeners.WorkoutBottomSheetListener

class WorkoutBottomSheet(
    private val listener : WorkoutBottomSheetListener,
    private val activeFilterDuration : MutableList<Int>?,
    private val activeFilterYear : Int?
) : BottomSheetDialogFragment() {

    companion object {
        val TAG = "WorkoutBottomSheet"
    }

    private var _binding : SheetWorkoutBinding? = null
    private val binding get() = _binding !!

    private var filterDuration : MutableList<Int>? = activeFilterDuration
    private var filterYear : Int? = activeFilterYear

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        _binding = SheetWorkoutBinding.inflate(inflater, container, false)
        val view = binding.root

        initActiveDurationFilter()
        initActiveYearFilter()

        addDurationChipGroupCheckedListener()
        addYearChipGroupCheckedListener()

        setApplyButtonListener()
        setClearButtonListener()

        return view
    }

    private fun initActiveDurationFilter() {
        binding.apply {
            val filterChip = when (activeFilterDuration) {
                mutableListOf(1, 14) -> chipDuration114Days
                mutableListOf(5, 25) -> chipDuration525Days
                mutableListOf(22, 28) -> chipDuration2228Days
                mutableListOf(29, 0) -> chipDuration29Days
                else -> chipDurationAll
            }
            filterChip.isChecked = true
        }

    }

    private fun initActiveYearFilter(){
        binding.apply {
            val filterChip = when (activeFilterYear) {
                2022 -> chipYear2022
                2021 -> chipYear2021
                2020 -> chipYear2020
                2019 -> chipYear2019
                2018 -> chipYear2018
                else -> chipYearAll
            }
            filterChip.isChecked = true
        }
    }

    private fun addDurationChipGroupCheckedListener() {
        binding.chipGroupDuration.setOnCheckedChangeListener { _, checkedId ->
            filterDuration = when (checkedId) {
                R.id.chipDuration1_14Days -> mutableListOf(1, 14)
                R.id.chipDuration5_25Days -> mutableListOf(5, 25)
                R.id.chipDuration22_28Days -> mutableListOf(22, 28)
                R.id.chipDuration29Days -> mutableListOf(29, 0)
                else -> null
            }
        }
    }

    private fun addYearChipGroupCheckedListener() {
        binding.chipGroupYear.setOnCheckedChangeListener { _, checkedId ->
            filterYear = when (checkedId) {
                R.id.chipYear2022 -> 2022
                R.id.chipYear2021 -> 2021
                R.id.chipYear2020 -> 2020
                R.id.chipYear2019 -> 2019
                R.id.chipYear2018 -> 2018
                else -> null
            }
        }
    }

    private fun setApplyButtonListener(){
        binding.buttonApply.setOnClickListener {
            listener.onFilterResults(
                filterYear, filterDuration
            )
            this@WorkoutBottomSheet.dismiss()
        }
    }

    private fun setClearButtonListener(){
        binding.buttonClear.setOnClickListener {
            filterYear = null
            filterDuration = null
            binding.chipYearAll.isChecked = true
            binding.chipDurationAll.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
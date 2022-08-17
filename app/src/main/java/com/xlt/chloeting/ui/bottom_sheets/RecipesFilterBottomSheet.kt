package com.xlt.chloeting.ui.bottom_sheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.xlt.chloeting.R
import com.xlt.chloeting.data.models.RecipeCategoriesModel
import com.xlt.chloeting.data.models.RecipeCategoryModel
import com.xlt.chloeting.databinding.SheetRecipesFilterBinding
import com.xlt.chloeting.ui.views.ChipWithId
import com.xlt.chloeting.ui.vm.RecipesViewModel
import com.xlt.chloeting.util.listeners.RecipesSheetListener

class RecipesFilterBottomSheet(
    private val viewModel : RecipesViewModel,
    private val listener: RecipesSheetListener
) : BottomSheetDialogFragment() {

    companion object {
        val TAG = "RecipesFilterBottomSheet"
    }

    private var _binding : SheetRecipesFilterBinding? = null
    private val binding get() = _binding !!
    private var filterCategories:RecipeCategoriesModel? = null

    private var tempFilterTime:String? = null
    private var tempFilterCourse:String? = null
    private var tempFilterDietary:String? = null
    private var tempFilterConvenience:String? = null


    private enum class FilterType {
        CURSE,
        DIETARY,
        TIME,
        CONVENIENCE
    }

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        _binding = SheetRecipesFilterBinding.inflate(inflater, container, false)
        val view = binding.root

        addApplyButtonListener()
        addClearFilterButtonListener()
        addChipGroupCheckedChipListener()

        checkCategories()

        return view
    }

    private fun checkCategories() {
        val categories = viewModel.categories.value
        if (categories == null) {
            Log.i(TAG, "categories = null")
            getCategoriesFromViewModel()
        }else {
            filterCategories = categories
            addChipsInGroupAndSafeIdInTemp()
        }
    }

    private fun getCategoriesFromViewModel(){
        showLoading()
        observeCategories()
        viewModel.getRecipeCategories()
    }

    private fun observeCategories(){
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (categories == null)
                showError()
            else {
                endLoading()
                filterCategories = categories

                if (binding.chipGroupCourses.childCount <= 1)
                    addChipsInGroupAndSafeIdInTemp()
            }
        }
    }

    private fun addChipsInGroupAndSafeIdInTemp() {
        binding.apply {
            filterCategories!!.coursesCategories.forEach {
                chipGroupCourses.addView(createChip(item = it, type = FilterType.CURSE))
            }
            filterCategories!!.convenienceCategories.forEach {
                chipGroupConvenience.addView(createChip(item = it, type = FilterType.CONVENIENCE))
            }
            filterCategories!!.dietaryRestrictionsCategories.forEach {
                chipGroupDietary.addView(createChip(item = it, type = FilterType.DIETARY))
            }
            filterCategories!!.cookingTimeCategories.forEach {
                chipGroupTime.addView(createChip(item = it, type = FilterType.TIME))
            }
        }
        //find checked ids and save them
        getActualCheckedView()
    }

    private fun createChip(item:RecipeCategoryModel, type:FilterType):Chip {
        val chip = ChipWithId(requireContext())

        chip.id = item.id
        chip.text = item.name
        chip.isCheckable = true
        chip.isChecked = checkFilterIsActive(item.id, type)

        chip.setChipBackgroundColorResource(R.color.colors_chip)
        chip.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.colors_chip_text))
        return chip
    }

    private fun checkFilterIsActive(id:String, type:FilterType):Boolean {
        return when (type) {
            FilterType.CURSE -> viewModel.courseFilter == id
            FilterType.DIETARY -> viewModel.dietaryFilter == id
            FilterType.TIME -> viewModel.timeFilter == id
            FilterType.CONVENIENCE -> viewModel.convenienceFilter == id
        }
    }

    private fun getActualCheckedView() {
        binding.apply {
            chipGroupTime.children.forEach {
                val chip:ChipWithId = it as ChipWithId
                if (chip.isChecked)
                    tempFilterTime = chip.id
            }
            chipGroupConvenience.children.forEach {
                val chip:ChipWithId = it as ChipWithId
                if (chip.isChecked)
                    tempFilterConvenience = chip.id
            }
            chipGroupCourses.children.forEach {
                val chip:ChipWithId = it as ChipWithId
                if (chip.isChecked)
                    tempFilterCourse = chip.id
            }
            chipGroupDietary.children.forEach {
                val chip:ChipWithId = it as ChipWithId
                if (chip.isChecked)
                    tempFilterDietary = chip.id
            }
        }
    }

    private fun addChipGroupCheckedChipListener() {
        binding.apply {
            chipGroupTime.setOnCheckedChangeListener { _, checkedId ->
                tempFilterTime = (root.findViewById<ChipWithId>(checkedId) ?: null)?.id
            }
            chipGroupDietary.setOnCheckedChangeListener { _, checkedId ->
                tempFilterDietary = (root.findViewById<ChipWithId>(checkedId) ?: null)?.id
            }
            chipGroupCourses.setOnCheckedChangeListener { _, checkedId ->
                tempFilterCourse = (root.findViewById<ChipWithId>(checkedId) ?: null)?.id
            }
            chipGroupConvenience.setOnCheckedChangeListener { _, checkedId ->
                tempFilterConvenience = (root.findViewById<ChipWithId>(checkedId) ?: null)?.id
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            content.isVisible = false
            errorView.isVisible = false
            loadingBar.isVisible = true
        }
    }

    private fun endLoading() {
        binding.apply {
            content.isVisible = true
            errorView.isVisible = false
            loadingBar.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            content.isVisible = false
            loadingBar.isVisible = false

            errorView.setError(null)
            errorView.isVisible = true
            errorView.addReturnClickListener() {
                getCategoriesFromViewModel()
            }
        }
    }

    private fun saveFilters() {
        viewModel.apply {
            timeFilter = tempFilterTime
            courseFilter = tempFilterCourse
            convenienceFilter = tempFilterConvenience
            dietaryFilter = tempFilterDietary
        }
    }

    private fun addApplyButtonListener() {
        binding.buttonApply.setOnClickListener {
            saveFilters()
            listener.onFilterResults()
            dismiss()
        }
    }

    private fun addClearFilterButtonListener() {
        binding.buttonClear.setOnClickListener {
            clearViewModelFilters()
            clearFilters()
        }
    }

    private fun clearFilters() {
        binding.apply {
            (chipGroupConvenience.get(0) as ChipWithId).isChecked  =true
            (chipGroupCourses.get(0) as ChipWithId).isChecked  =true
            (chipGroupTime.get(0) as ChipWithId).isChecked  =true
            (chipGroupDietary.get(0) as ChipWithId).isChecked  =true
        }
    }

    private fun clearViewModelFilters(){
        viewModel.apply {
            timeFilter = null
            courseFilter = null
            dietaryFilter = null
            convenienceFilter = null
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
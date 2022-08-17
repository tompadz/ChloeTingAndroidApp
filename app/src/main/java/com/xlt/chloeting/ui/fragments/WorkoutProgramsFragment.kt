package com.xlt.chloeting.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xlt.chloeting.R
import com.xlt.chloeting.ui.activities.MainActivity
import com.xlt.chloeting.data.adapters.WorkoutProgramsAdapter
import com.xlt.chloeting.databinding.FragmentWorkoutProgramsBinding
import com.xlt.chloeting.ui.bottom_sheets.WorkoutBottomSheet
import com.xlt.chloeting.ui.vm.WorkoutProgramsViewModel
import com.xlt.chloeting.util.PagingLoadingListener
import com.xlt.chloeting.ui.decorators.SpacesItemDecoration
import com.xlt.chloeting.ui.views.TabLayoutWithFilter
import com.xlt.chloeting.util.FilterProgramType
import com.xlt.chloeting.util.listeners.WorkoutBottomSheetListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WorkoutProgramsFragment : Fragment(), WorkoutBottomSheetListener {

    private val TAG = "WorkoutProgramsFragment"

    private var _binding:FragmentWorkoutProgramsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WorkoutProgramsViewModel by viewModels()
    private lateinit var adapter:WorkoutProgramsAdapter

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        _binding = FragmentWorkoutProgramsBinding.inflate(inflater, container, false)
        val view = binding.root

        setToolbarNavigationClickAction()
        setToolbarMenuClickListener()
        initAdapterAndSetRecyclerViewParams()
        addPagingLoadingListener()
        addTabLayoutClickListener()
        observeWorkoutProgram()
        updateTabFilter()

        return view
    }


    private fun updateTabFilter() {
        binding.tabLayout.setActiveTab(viewModel.program)
    }

    private fun setToolbarNavigationClickAction() {
        val activity = requireActivity()
        if (activity !is MainActivity) return
        binding.toolbar.setNavigationOnClickListener {
            activity.openDrawer()
        }
    }

    private fun setToolbarMenuClickListener() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter -> {
                    openFilterBottomSheet()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
    }

    private fun addTabLayoutClickListener(){
        binding.apply {
            tabLayout.addFilterChangeListener(object : TabLayoutWithFilter.FilterChangeListener {
                override fun onFilterChange(filter : FilterProgramType) {
                    Log.i(TAG, "program filter - ${filter.name}")
                    if (filter != viewModel.program) {
                        viewModel.program = filter
                        viewModel.getWorkoutPrograms()
                    }
                }
            })
        }
    }

    private fun initAdapterAndSetRecyclerViewParams(){
        adapter = WorkoutProgramsAdapter()

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            val decoration = SpacesItemDecoration(200)
            recyclerView.addItemDecoration(decoration)
        }
    }

    private fun addPagingLoadingListener(){
        PagingLoadingListener().addPagingLoadingListener(
            adapter = adapter,
            recyclerView = binding.recyclerView,
            loadingBar = binding.loadingBar,
            errorView = binding.errorView,
            emptyMessage = binding.emptyText
        )
    }

    private fun observeWorkoutProgram(){
        viewModel.programs.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun openFilterBottomSheet(){
        val sheet = WorkoutBottomSheet(
            listener = this,
            activeFilterDuration = viewModel.duration as MutableList<Int>?,
            activeFilterYear = viewModel.year
        )
        sheet.show(parentFragmentManager, WorkoutBottomSheet.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFilterResults(filterYear : Int?, filterDuration : List<Int>?) {
        Log.i(TAG, "filter year - $filterYear")
        Log.i(TAG, "filter duration - $filterDuration")
        viewModel.year = filterYear
        viewModel.duration = filterDuration
        viewModel.getWorkoutPrograms()
    }

}
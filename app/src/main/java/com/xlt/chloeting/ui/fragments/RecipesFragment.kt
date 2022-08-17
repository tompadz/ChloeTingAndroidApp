package com.xlt.chloeting.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xlt.chloeting.R
import com.xlt.chloeting.data.adapters.RecipesAdapter
import com.xlt.chloeting.data.adapters.WorkoutProgramsAdapter
import com.xlt.chloeting.databinding.FragmentRecipesBinding
import com.xlt.chloeting.databinding.FragmentWorkoutProgramsBinding
import com.xlt.chloeting.ui.activities.MainActivity
import com.xlt.chloeting.ui.bottom_sheets.RecipesFilterBottomSheet
import com.xlt.chloeting.ui.decorators.SpacesItemDecoration
import com.xlt.chloeting.ui.views.SearchView
import com.xlt.chloeting.ui.vm.RecipesViewModel
import com.xlt.chloeting.ui.vm.WorkoutProgramsViewModel
import com.xlt.chloeting.util.PagingLoadingListener
import com.xlt.chloeting.util.listeners.RecipesSheetListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment(), RecipesSheetListener {

    private val TAG = "RecipesFragment"

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var adapter: RecipesAdapter

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        _binding = FragmentRecipesBinding.inflate(layoutInflater,container, false)
        val view = binding.root

        addSearchListener()
        setToolbarNavigationClickAction()
        setToolbarMenuClickListener()
        initAdapterAndSetRecyclerViewParams()
        addPagingLoadingListener()
        observeRecipes()

        return view
    }


    private fun setToolbarNavigationClickAction() {
        val activity = requireActivity()
        if (activity !is MainActivity) return
        binding.toolbar.setNavigationOnClickListener {
            activity.openDrawer()
        }
    }

    private fun initAdapterAndSetRecyclerViewParams(){
        adapter = RecipesAdapter()
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

    private fun addSearchListener() {
       binding.searchInput.setOnSearchListener(object : SearchView.SearchListener {
           override fun onQueryChange(query : String) {
               if (query != viewModel.search) {
                   viewModel.search = query
                   viewModel.getRecipes()
               }
           }
       })
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

    private fun openFilterBottomSheet() {
        val sheet = RecipesFilterBottomSheet(
            viewModel,
            this,
        )
        sheet.show(parentFragmentManager, RecipesFilterBottomSheet.TAG)
    }


    private fun observeRecipes(){
        viewModel.programs.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFilterResults() {
        viewModel.getRecipes()
    }
}
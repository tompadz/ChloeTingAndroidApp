package com.xlt.chloeting.util

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xlt.chloeting.data.adapters.BaseLoadStateAdapter
import com.xlt.chloeting.ui.views.ErrorView

class PagingLoadingListener {

    val TAG = "PAGING_LOADING_LISTENER"

    fun <T:Any, VH: RecyclerView.ViewHolder> addPagingLoadingListener(
        adapter: PagingDataAdapter<T, VH>,
        loadingBar:ProgressBar,
        recyclerView : RecyclerView,
        errorView : ErrorView,
        emptyMessage:View
    ) {

        recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter() { adapter.retry() },
            footer = BaseLoadStateAdapter() { adapter.retry() }
        )

        adapter.addLoadStateListener { loadingState ->

            val error = when {
                loadingState.prepend is LoadState.Error -> loadingState.prepend as LoadState.Error
                loadingState.append is LoadState.Error -> loadingState.append as LoadState.Error
                loadingState.refresh is LoadState.Error -> loadingState.refresh as LoadState.Error
                else -> null
            }

            when (loadingState.source.refresh) {
                is LoadState.NotLoading -> {
                    loadingBar.isVisible = false
                    errorView.isVisible = false
                    if (adapter.itemCount > 0) {
                        recyclerView.isVisible = true
                        emptyMessage.isVisible = false
                    }else {
                        recyclerView.isVisible = false
                        emptyMessage.isVisible = true
                    }
                    Log.i(TAG, "end loading")
                }
                is LoadState.Loading -> {
                    loadingBar.isVisible = true
                    recyclerView.isVisible = false
                    errorView.isVisible = false
                    emptyMessage.isVisible = false
                    Log.i(TAG, "start loading")
                }
                is LoadState.Error -> {
                    loadingBar.isVisible = false
                    recyclerView.isVisible = false
                    errorView.isVisible = true
                    errorView.setError(error?.error)
                    errorView.addReturnClickListener {
                        adapter.retry()
                    }
                }
            }
        }
    }
}
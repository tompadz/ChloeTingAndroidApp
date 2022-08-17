package com.xlt.chloeting.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xlt.chloeting.databinding.WidgetPagingFooterBinding

class BaseLoadStateAdapter(private val retry : () -> Unit) :
    LoadStateAdapter<BaseLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding : WidgetPagingFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState : LoadState) {
            binding.apply {
                loadingBar.isVisible = loadState is LoadState.Loading
                messageError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder : LoadStateViewHolder, loadState : LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent : ViewGroup,
        loadState : LoadState
    ) : LoadStateViewHolder {
        val binding =
            WidgetPagingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }
}

package com.xlt.chloeting.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xlt.chloeting.databinding.ItemWorkoutProgramBinding
import com.xlt.chloeting.data.models.ProgramModel

class WorkoutProgramsAdapter : PagingDataAdapter<ProgramModel, WorkoutProgramsAdapter.ProgramViewHolder>(PROGRAM_COMPARATOR) {

    inner class ProgramViewHolder(val binding : ItemWorkoutProgramBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder : ProgramViewHolder, position : Int) {
        val item = getItem(position) ?: return
        with(holder) {
            binding.apply {
                Glide.with(image).load(item.bannerImageUrl).into(image)
                days.text = "${item.duration} d"
                title.text = item.name
                timeInfo.text = "${item.avgMinsPerDay} min/day"
                newContainer.isVisible = item.tagNew
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ProgramViewHolder {
        val binding = ItemWorkoutProgramBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProgramViewHolder(binding)
    }

    private companion object {
        private val PROGRAM_COMPARATOR = object : DiffUtil.ItemCallback<ProgramModel>() {
            override fun areItemsTheSame(oldItem : ProgramModel, newItem : ProgramModel) : Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem : ProgramModel, newItem : ProgramModel) : Boolean {
                return oldItem == newItem
            }
        }
    }

}
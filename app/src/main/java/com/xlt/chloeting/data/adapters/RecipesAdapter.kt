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
import com.xlt.chloeting.data.models.RecipeModel
import com.xlt.chloeting.databinding.ItemRecipesBinding

class RecipesAdapter : PagingDataAdapter<RecipeModel, RecipesAdapter.RecipesViewHolder>(RECIPES_COMPARATOR) {

    inner class RecipesViewHolder(val binding : ItemRecipesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder : RecipesViewHolder, position : Int) {
        val item = getItem(position) ?: return
        with(holder) {
            binding.apply {
                Glide.with(image).load(item.coverImage.webpImageUrl).into(image)
                title.text = item.title
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RecipesViewHolder {
        val binding = ItemRecipesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecipesViewHolder(binding)
    }

    private companion object {
        private val RECIPES_COMPARATOR = object : DiffUtil.ItemCallback<RecipeModel>() {
            override fun areItemsTheSame(oldItem : RecipeModel, newItem : RecipeModel) : Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem : RecipeModel, newItem : RecipeModel) : Boolean {
                return oldItem == newItem
            }
        }
    }

}
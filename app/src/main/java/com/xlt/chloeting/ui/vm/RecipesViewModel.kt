package com.xlt.chloeting.ui.vm

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.versionedparcelable.ParcelField
import com.xlt.chloeting.data.models.RecipeCategoriesModel
import com.xlt.chloeting.data.repository.RecipesRepository
import com.xlt.chloeting.data.repository.WorkoutProgramsRepository
import com.xlt.chloeting.util.FilterProgramType
import com.xlt.chloeting.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val repository : RecipesRepository,
    private val state : SavedStateHandle,
) : ViewModel() {

    private val TAG = "RecipesViewModel"

    private var filter = Filter()

    @Parcelize
    data class Filter(
        var search : String? = null,
        val coursesCategory : String? = null,
        val convenienceCategory : String? = null,
        val dietaryRestrictionsCategories : String? = null,
        val cookingTimeCategory : String? = null,
    ) : Parcelable

    var search = state.get<String?>("state_search")
        set(value) {
            field = value
            state["state_search"] = value
        }

    var courseFilter = state.get<String?>("state_course")
        set(value) {
            field = value
            state["state_course"] = value
        }

    var convenienceFilter = state.get<String?>("state_convenience")
        set(value) {
            field = value
            state["state_convenience"] = value
        }

    var dietaryFilter = state.get<String?>("state_dietary")
        set(value) {
            field = value
            state["state_dietary"] = value
        }

    var timeFilter = state.get<String?>("state_time")
        set(value) {
            field = value
            state["state_time"] = value
        }

    private val _recipes = state.getLiveData("queryRecipes", filter)

    private val _categories : MutableLiveData<RecipeCategoriesModel?> = MutableLiveData()
    val categories : LiveData<RecipeCategoriesModel?> = _categories

    init {
        getRecipeCategories()
    }

    val programs = _recipes.switchMap {
        repository.getRecipes(
            search = it.search,
            coursesCategory = it.coursesCategory,
            convenienceCategory = it.convenienceCategory,
            dietaryRestrictionsCategories = it.dietaryRestrictionsCategories,
            cookingTimeCategory = it.cookingTimeCategory
        ).cachedIn(viewModelScope)
    }

    fun getRecipeCategories() {
        viewModelScope.launch {
            when (val result = repository.getRecipesCategories()) {
                is ResultWrapper.Success -> {
                    _categories.value = result.value
                }
                is ResultWrapper.Error -> {
                    Log.e(TAG, result.error?.localizedMessage ?: "unknown error")
                    _categories.value = null
                }
            }
        }
    }

    fun getRecipes() {
        filter = Filter(
            search = search,
            coursesCategory = courseFilter,
            convenienceCategory = convenienceFilter,
            dietaryRestrictionsCategories = dietaryFilter,
            cookingTimeCategory = timeFilter
        )
        _recipes.value = filter
    }

}
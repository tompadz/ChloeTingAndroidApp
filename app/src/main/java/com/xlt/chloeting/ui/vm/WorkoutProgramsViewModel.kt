package com.xlt.chloeting.ui.vm

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.versionedparcelable.ParcelField
import com.xlt.chloeting.data.repository.WorkoutProgramsRepository
import com.xlt.chloeting.util.FilterProgramType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class WorkoutProgramsViewModel @Inject constructor(
    private val repository : WorkoutProgramsRepository,
    private val state : SavedStateHandle,
) : ViewModel() {

    @Parcelize
    data class Filter(
        val duration:List<Int>? = null,
        val year : Int? = null,
        val programType : FilterProgramType = FilterProgramType.ALL
    ) : Parcelable

    private var filter = Filter()

    var duration = state.get<List<Int>?>("state_filter_duration")
        set(value) {
            field = value
            state["state_filter_duration"] = value
        }

    var year = state.get<Int?>("state_filter_year")
        set(value) {
            field = value
            state["state_filter_year"] = value
        }

    var program = state.get<FilterProgramType>("state_filter_program") ?: filter.programType
        set(value) {
            field = value
            state["state_filter_program"] = value
        }

    private val _programs = state.getLiveData("queryPrograms", filter)

    val programs = _programs.switchMap {
        repository.getWorkoutPrograms(filter.duration, filter.year, filter.programType).cachedIn(viewModelScope)
    }

    fun getWorkoutPrograms() {
        filter = Filter(
            duration,
            year,
            program
        )
        _programs.value = filter
    }



}
package com.xlt.chloeting.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.xlt.chloeting.data.api.WorkoutProgramsApi
import com.xlt.chloeting.util.FilterProgramType
import com.xlt.chloeting.util.ResponcePagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutProgramsRepository @Inject constructor(private val api: WorkoutProgramsApi): BaseRepository() {

    private val PAGE_SIZE = 5
    fun getWorkoutPrograms(
        duration:List<Int>?,
        year:Int?,
        programType : FilterProgramType = FilterProgramType.ALL
    ) =
        Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        ),
            pagingSourceFactory = {
                ResponcePagingSource(
                    pageSize = PAGE_SIZE,
                ) {
                    api.getWorkupPrograms(page = it, perPage = PAGE_SIZE, durationMinDay = duration?.get(0), durationMaxDay = duration?.get(1), year = year, type = programType.type)
                }
            }
        ).liveData

}
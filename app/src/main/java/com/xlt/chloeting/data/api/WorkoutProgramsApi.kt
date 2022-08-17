package com.xlt.chloeting.data.api

import com.xlt.chloeting.data.models.ProgramModel
import com.xlt.chloeting.data.models.ResponceBodyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WorkoutProgramsApi {
    @GET("app/program")
    suspend fun getWorkupPrograms(
        @Query("page")page:Int,
        @Query("perPage")perPage:Int = 10,
        @Query("durationMinDay")durationMinDay:Int? = null,
        @Query("durationMaxDay")durationMaxDay:Int? = null,
        @Query("type")type:String? = null,
        @Query("year")year:Int? = null,
    ):Response<ResponceBodyModel<ProgramModel>>
}
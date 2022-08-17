package com.xlt.chloeting.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.xlt.chloeting.data.api.RecipesApi
import com.xlt.chloeting.data.models.RecipeCategoriesModel
import com.xlt.chloeting.util.FilterProgramType
import com.xlt.chloeting.util.ResponcePagingSource
import com.xlt.chloeting.util.ResultWrapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepository @Inject constructor(private val api: RecipesApi): BaseRepository() {

    suspend fun getRecipesCategories(): ResultWrapper<RecipeCategoriesModel> {
        return safeApiCall() {
           api.getRecipesCategory()
        }
    }

    private val PAGE_SIZE = 10
    fun getRecipes(
        search:String?,
        coursesCategory:String?,
        convenienceCategory:String?,
        dietaryRestrictionsCategories:String?,
        cookingTimeCategory:String?,
    ) =
        Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        ),
            pagingSourceFactory = {
                ResponcePagingSource(
                    pageSize = PAGE_SIZE,
                ) {
                    api.getRecipes(
                        page = it,
                        perPage = PAGE_SIZE,
                        search = search,
                        coursesCategory = coursesCategory,
                        convenienceCategory =  convenienceCategory,
                        dietaryRestrictionsCategories =  dietaryRestrictionsCategories,
                        cookingTimeCategory = cookingTimeCategory
                    )
                }
            }
        ).liveData

}
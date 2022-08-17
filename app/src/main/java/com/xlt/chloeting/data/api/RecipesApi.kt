package com.xlt.chloeting.data.api

import com.xlt.chloeting.data.models.RecipeCategoriesModel
import com.xlt.chloeting.data.models.RecipeModel
import com.xlt.chloeting.data.models.ResponceBodyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApi {

    @GET("app/recipe")
    suspend fun getRecipes(
        @Query("page")page:Int,
        @Query("perPage")perPage:Int = 15,
        @Query("search")search:String? = null,
        @Query("sort")sort:String? = null,
        @Query("coursesCategory")coursesCategory:String? = null,
        @Query("convenienceCategory")convenienceCategory:String? = null,
        @Query("dietaryRestrictionsCategories")dietaryRestrictionsCategories:String? = null,
        @Query("cookingTimeCategory")cookingTimeCategory:String? = null
    ): Response<ResponceBodyModel<RecipeModel>>

    @GET("app/recipe/filter")
    suspend fun getRecipesCategory(): Response<RecipeCategoriesModel>
}
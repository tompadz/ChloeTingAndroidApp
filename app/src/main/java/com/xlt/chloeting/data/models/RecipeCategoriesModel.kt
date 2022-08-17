package com.xlt.chloeting.data.models

data class RecipeCategoriesModel(
    val coursesCategories:List<RecipeCategoryModel>,
    val convenienceCategories:List<RecipeCategoryModel>,
    val dietaryRestrictionsCategories:List<RecipeCategoryModel>,
    val cookingTimeCategories:List<RecipeCategoryModel>,
)

data class RecipeCategoryModel(
    val id:String,
    val name:String
)

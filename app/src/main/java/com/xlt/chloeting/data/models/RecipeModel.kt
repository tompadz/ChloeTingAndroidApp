package com.xlt.chloeting.data.models

data class RecipeModel(
    val id:String,
    val title:String,
    val slug:String,
    val coverImage: CoverImageModel,
)

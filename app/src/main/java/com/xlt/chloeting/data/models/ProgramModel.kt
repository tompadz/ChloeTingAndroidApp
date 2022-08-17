package com.xlt.chloeting.data.models

data class ProgramModel(
    val id : String,
    val slug : String,
    val bannerImageUrl : String,
    val name : String,
    val duration : Int,
    val avgMinsPerDay : String,
    val types : List<String>,
    val equipments : List<String>,
    val publishedDate : String,
    val startedAt : String,
    val youtubePlaylistUrl : List<String>,
    val faqs : List<FaqsModels>,
    val playlistLinks : List<String>,
    val tagNew : Boolean
)

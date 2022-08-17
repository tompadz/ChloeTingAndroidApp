package com.xlt.chloeting.data.models

data class ResponceBodyModel <out T> (
    val items:List<T>,
    val page:Int,
    val perPage:Int,
    val pageCount:Int,
    val total:Int,
    val statusCode:Int?,
    val message:String?
)

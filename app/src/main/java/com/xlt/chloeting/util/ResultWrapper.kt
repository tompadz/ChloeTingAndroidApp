package com.xlt.chloeting.util

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class Error(val code : Int? = null, val error : kotlin.Error? = null): ResultWrapper<Nothing>()
}
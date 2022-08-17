package com.xlt.chloeting.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.xlt.chloeting.util.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

open class BaseRepository {
    
    private val TAG = "BASE_REPOSITORY"
    
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> Response<T>): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                checkResponceCodeAndReturnResult(apiCall)
            }catch (throwable: Throwable){
                Log.e(TAG, throwable.message?:"error")
                checkThrowableAndReturnResult(throwable)
            }
        }
    }

    private suspend fun <T> checkResponceCodeAndReturnResult(apiCall: suspend () -> Response<T>):ResultWrapper<T> {
        val response = apiCall.invoke()
        return if (response.isSuccessful){
            val responceBody = apiCall.invoke().body()!!
            ResultWrapper.Success(responceBody)
        }else{
            val code = response.code()
            val errorResponse = convertErrorBody(errorText = response.errorBody().toString())
            Log.e(TAG, errorResponse?.message?:"error")
            ResultWrapper.Error(code, errorResponse)
        }
    }

    private fun <T> checkThrowableAndReturnResult(throwable: Throwable):ResultWrapper<T>{
        return when (throwable){
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = convertErrorBody(throwable)
                ResultWrapper.Error(code, errorResponse)
            }
            else -> ResultWrapper.Error()
        }
    }

    private fun convertErrorBody(throwable: HttpException): Error? {
        val body = throwable.response()?.errorBody()
        val gson = Gson()
        val adapter: TypeAdapter<Error> = gson.getAdapter(Error::class.java)
        return try {
            val error: Error = adapter.fromJson(body?.string())
            error
        }catch (exception: Exception){
            null
        }
    }

    private fun convertErrorBody(errorText: String): Error? {
        val gson = Gson()
        val adapter: TypeAdapter<Error> = gson.getAdapter(Error::class.java)
        return try {
            val error: Error = adapter.fromJson(errorText)
            error
        }catch (exception: Exception){
            null
        }
    }
}
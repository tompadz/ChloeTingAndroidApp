package com.xlt.chloeting.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xlt.chloeting.data.models.ResponceBodyModel
import retrofit2.HttpException
import retrofit2.Response

class ResponcePagingSource<T:Any>(
    private val pageSize : Int,
    private val apiCall : suspend (offset : Int) -> Response<ResponceBodyModel<T>>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state : PagingState<Int, T>) : Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params : LoadParams<Int>) : LoadResult<Int, T> {
        return try {

            val nextPageNumber = params.key ?: 1
            val response = apiCall.invoke(nextPageNumber)
            if (!response.isSuccessful) throw HttpException(response)
            val result = response.body()!!

            return LoadResult.Page(
                data = result.items,
                prevKey = null,
                nextKey = if (result.page < result.pageCount) nextPageNumber + 1 else null
            )

        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

}
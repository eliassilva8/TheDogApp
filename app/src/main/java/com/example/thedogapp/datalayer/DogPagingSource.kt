package com.example.thedogapp.datalayer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.thedogapp.BuildConfig
import retrofit2.await
import retrofit2.awaitResponse
import javax.inject.Inject

class DogPagingSource @Inject constructor(
    private val theDogApi: TheDogApi
): PagingSource<Int, DogDataModel>() {

    private val apiKey = BuildConfig.THE_DOG_API_KEY

    override fun getRefreshKey(state: PagingState<Int, DogDataModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        return  anchorPosition - state.config.pageSize / 2
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DogDataModel> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val response = theDogApi.getDogs("10", "true").awaitResponse()

            if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()!!,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page < pageSize) page + 1 else null
                )
            } else {
                LoadResult.Error(ApiResponseFailedException("Error searching for dogs"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
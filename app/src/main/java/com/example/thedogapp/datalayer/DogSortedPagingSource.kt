package com.example.thedogapp.datalayer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.thedogapp.BuildConfig
import retrofit2.awaitResponse
import javax.inject.Inject

class DogSortedPagingSource @Inject constructor(
    private val theDogApi: TheDogApi
): PagingSource<Int, DogDataModel>() {

    private val apiKey = BuildConfig.THE_DOG_API_KEY

    override fun getRefreshKey(state: PagingState<Int, DogDataModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        return  anchorPosition - state.config.pageSize / 2
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DogDataModel> {
        return try {
            val page = params.key ?: 0
            val loadSize = params.loadSize
            val response = theDogApi.getDogsSorted(loadSize.toString(), page.toString(), "true", "ASC").awaitResponse()

            if (response.isSuccessful) {
                val hasMoreItems = response.body()!!.size == loadSize
                val nextPage = if (hasMoreItems) {
                    page + 1
                } else {
                    null
                }

                LoadResult.Page(
                    data = response.body()!!,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(ApiResponseFailedException("Error searching for dogs sorted"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
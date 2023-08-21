package com.example.thedogapp.datalayer.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.thedogapp.BuildConfig
import com.example.thedogapp.datalayer.models.DogDataModel
import com.example.thedogapp.datalayer.api.TheDogApi
import javax.inject.Inject

class DogSearchPagingSource @Inject constructor(
    private val theDogApi: TheDogApi,
    private val query: String?
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
            val response = theDogApi.searchDogs(apiKey, query!!)

            if (response.isEmpty()) {
                LoadResult.Error(ApiEmptyResponseException("No dogs found"))
            } else {
                val searchResult = mutableListOf<DogDataModel>()
                val hasMoreItems = response.size == loadSize
                val nextPage = if (hasMoreItems) {
                    page + 1
                } else {
                    null
                }

                response.forEach {breed ->
                    val dogData = theDogApi.getDogByImageId(breed.imageId)
                    searchResult.add(dogData)
                }

                LoadResult.Page(
                    data = searchResult,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = nextPage
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(ApiResponseFailedException("Error getting dogs"))
        }
    }
}
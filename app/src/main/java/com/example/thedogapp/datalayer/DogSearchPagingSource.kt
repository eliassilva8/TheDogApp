package com.example.thedogapp.datalayer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.thedogapp.BuildConfig
import retrofit2.awaitResponse
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
            val response = theDogApi.searchDogs(query!!).awaitResponse()

            if (response.isSuccessful) {
                val items = response.body()!!
                val searchResult = mutableListOf<DogDataModel>()
                val hasMoreItems = items.size == loadSize
                val nextPage = if (hasMoreItems) {
                    page + 1
                } else {
                    null
                }

                items.forEach {breed ->
                    val dogData = theDogApi.getDogByImageId(breed.imageId).awaitResponse()

                    if (response.isSuccessful) {
                        searchResult.add(dogData.body()!!)
                    }
                }

                LoadResult.Page(
                    data = searchResult,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(ApiResponseFailedException("Error searching for dogs"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
package com.example.thedogapp.datalayer.sources

import com.example.thedogapp.BuildConfig
import com.example.thedogapp.datalayer.models.DogDataModel
import com.example.thedogapp.datalayer.api.TheDogApi
import javax.inject.Inject

class DogSearchPagingSource @Inject constructor(
    private val theDogApi: TheDogApi,
    private val query: String?
): BasePagingSource() {
    private val apiKey = BuildConfig.THE_DOG_API_KEY

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DogDataModel> {
        return try {
            val page = params.key ?: 0
            val loadSize = params.loadSize
            val response = theDogApi.searchDogs(apiKey, query!!)
            val searchResult = mutableListOf<DogDataModel>()

            response.forEach {breed ->
                val dogData = theDogApi.getDogByImageId(breed.imageId)
                searchResult.add(dogData)
            }

            super.getLoadResult(searchResult, loadSize, page)
        } catch (e: Exception) {
            LoadResult.Error(ApiResponseFailedException("Error getting dogs"))
        }
    }
}
package com.example.thedogapp.datalayer.repositories

import com.example.thedogapp.datalayer.api.TheDogApi
import com.example.thedogapp.datalayer.sources.DogPagingSource
import com.example.thedogapp.datalayer.sources.DogSearchPagingSource
import com.example.thedogapp.datalayer.sources.DogSortedPagingSource
import javax.inject.Inject

class TheDogApiRepository @Inject constructor(
    private val theDogApi: TheDogApi
) {
    fun getDogs() = DogPagingSource(theDogApi)

    fun getDogsSorted() = DogSortedPagingSource(theDogApi)

    fun searchDogs(query: String?) = DogSearchPagingSource(theDogApi, query)
}
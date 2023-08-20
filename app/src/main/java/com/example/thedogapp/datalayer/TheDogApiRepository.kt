package com.example.thedogapp.datalayer

import javax.inject.Inject

class TheDogApiRepository @Inject constructor(
    private val theDogApi: TheDogApi
) {
    fun getDogs() = DogPagingSource(theDogApi)

    fun getDogsSorted() = DogSortedPagingSource(theDogApi)

    fun searchDogs(query: String?) = DogSearchPagingSource(theDogApi, query)
}
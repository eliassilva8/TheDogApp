package com.example.thedogapp.datalayer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheDogApi {
    @GET("search")
    fun getDogs(
        @Query("limit") limit: String
    ): Call<List<DogDataModel>>
}
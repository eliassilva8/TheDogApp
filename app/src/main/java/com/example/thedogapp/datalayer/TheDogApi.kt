package com.example.thedogapp.datalayer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface TheDogApi {
    @GET("search")
    fun getDogs(
        //@Header("x-api-key") key: String,
        @Query("limit") limit: String,
        @Query("has_breeds") hasBreeds: String
    ): Call<List<DogDataModel>>
}
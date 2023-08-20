package com.example.thedogapp.datalayer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheDogApi {
    @GET("images/search")
    fun getDogs(
        //@Header("x-api-key") key: String,
        @Query("limit") limit: String,
        @Query("page") page: String,
        @Query("has_breeds") hasBreeds: String
    ): Call<List<DogDataModel>>

    @GET("images/search")
    fun getDogsSorted(
        //@Header("x-api-key") key: String,
        @Query("limit") limit: String,
        @Query("page") page: String,
        @Query("has_breeds") hasBreeds: String,
        @Query("order") order: String
    ): Call<List<DogDataModel>>

    @GET("images/{image_id}")
    fun getDogByImageId(
        @Path("image_id") imageId: String
    ): Call<DogDataModel>

    @GET("breeds/search")
    fun searchDogs(
        //@Header("x-api-key") key: String,
        @Query("q") query: String
    ): Call<List<Breed>>
}
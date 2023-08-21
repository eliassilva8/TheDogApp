package com.example.thedogapp.datalayer.api

import com.example.thedogapp.datalayer.models.Breed
import com.example.thedogapp.datalayer.models.DogDataModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TheDogApi {
    @GET("images/search")
    suspend fun getDogs(
        @Header("x-api-key") key: String,
        @Query("limit") limit: String,
        @Query("page") page: String,
        @Query("has_breeds") hasBreeds: String
    ): List<DogDataModel>

    @GET("images/search")
    suspend fun getDogsSorted(
        @Header("x-api-key") key: String,
        @Query("limit") limit: String,
        @Query("page") page: String,
        @Query("has_breeds") hasBreeds: String,
        @Query("order") order: String
    ): List<DogDataModel>

    @GET("images/{image_id}")
    suspend fun getDogByImageId(
        @Path("image_id") imageId: String
    ): DogDataModel

    @GET("breeds/search")
    suspend fun searchDogs(
        @Header("x-api-key") key: String,
        @Query("q") query: String
    ): List<Breed>
}
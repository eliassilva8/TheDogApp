package com.example.thedogapp.datalayer

import com.example.thedogapp.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class TheDogApiRepository @Inject constructor(
    private val theDogApi: TheDogApi
) {
    private val apiKey = BuildConfig.THE_DOG_API_KEY

    suspend fun getDogs(): Flow<List<DogDataModel>> {
        return flow {
            val call = theDogApi.getDogs(
                limit = "10"
            )

            val response = call.awaitResponse()

            if (response.isSuccessful) {
                emit(response.body()!!)
            } else {
                throw ApiResponseFailedException("Error searching for dogs")
            }
        }
    }
}
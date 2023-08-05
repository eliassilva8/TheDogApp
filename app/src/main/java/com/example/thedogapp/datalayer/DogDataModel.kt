package com.example.thedogapp.datalayer

import com.example.thedogapp.presentationlayer.ui.DogUiModel
import com.google.gson.annotations.SerializedName

data class DogDataModel(
    val id: String,
    val url: String,
    val breeds: Breed,
) {
    data class Breed(
        val weight: String,
        val id: String,
        val temperament: String,
        val origin: String,
        @SerializedName(value = "country_codes")
        val countryCodes: String,
        @SerializedName(value = "country_code")
        val countryCode: String,
        @SerializedName(value = "life_span")
        val lifeSpan: String,
        @SerializedName(value = "wikipedia_url")
        val wikipediaUrl: String
    )
}

fun DogDataModel.toDogUiModel(): DogUiModel {
    return DogUiModel(
        id = id,
        imageUrl = url,
        name = id
    )
}

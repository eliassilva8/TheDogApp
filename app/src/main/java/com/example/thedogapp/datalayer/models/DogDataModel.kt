package com.example.thedogapp.datalayer.models

import com.example.thedogapp.presentationlayer.models.DogUiModel
import com.google.gson.annotations.SerializedName

data class DogDataModel(
    val breeds: List<Breed>?,
    val id: String,
    val url: String?
)
data class Breed(
    val name: String,
    @SerializedName(value = "breed_group")
    val group: String,
    val origin: String,
    val temperament: String,
    @SerializedName(value = "reference_image_id")
    val imageId: String,
)

fun DogDataModel.toDogUiModel(): DogUiModel {
    return DogUiModel(
        id = id,
        imageUrl = url ?: "",
        name = breeds?.get(0)?.name ?: "",
        group = breeds?.get(0)?.group ?: "",
        origin = breeds?.get(0)?.origin ?: "",
        temperament = breeds?.get(0)?.temperament ?: ""
    )
}

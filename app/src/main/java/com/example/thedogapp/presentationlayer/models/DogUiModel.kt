package com.example.thedogapp.presentationlayer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogUiModel(
    val id: String,
    val imageUrl: String,
    val name: String,
    val group: String,
    val origin: String,
    val temperament: String
    ): Parcelable

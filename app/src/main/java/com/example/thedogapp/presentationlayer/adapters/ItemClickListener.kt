package com.example.thedogapp.presentationlayer.adapters

import com.example.thedogapp.presentationlayer.models.DogUiModel

interface ItemClickListener {
    fun onClick(item: DogUiModel)
}
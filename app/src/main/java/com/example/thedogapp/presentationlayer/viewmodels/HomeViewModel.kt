package com.example.thedogapp.presentationlayer.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedogapp.datalayer.TheDogApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theDogApiRepository: TheDogApiRepository
) : ViewModel() {

    fun fetchDogs() {
        viewModelScope.launch(Dispatchers.IO) {
            theDogApiRepository.getDogs().collect {
                Log.d("HomeViewModel", "List of dogs: ${it.firstOrNull()}")
            }
        }
    }
}
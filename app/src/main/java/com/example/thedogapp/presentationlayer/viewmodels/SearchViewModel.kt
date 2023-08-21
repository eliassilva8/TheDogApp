package com.example.thedogapp.presentationlayer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.thedogapp.datalayer.repositories.TheDogApiRepository
import com.example.thedogapp.datalayer.models.toDogUiModel
import com.example.thedogapp.presentationlayer.models.DogUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val ITEM_PER_PAGE = 7

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val theDogApiRepository: TheDogApiRepository
): ViewModel() {

    fun searchDogs(query: String?): Flow<PagingData<DogUiModel>> = Pager(
        config = PagingConfig(
            pageSize = ITEM_PER_PAGE,
            enablePlaceholders = false,
            initialLoadSize = ITEM_PER_PAGE
        ),
        pagingSourceFactory = {
            theDogApiRepository.searchDogs(query)
        }
    )
        .flow
        .map { pagingData ->
            pagingData.map { breed ->
                breed.toDogUiModel()
            }
        }
        .cachedIn(viewModelScope)
}
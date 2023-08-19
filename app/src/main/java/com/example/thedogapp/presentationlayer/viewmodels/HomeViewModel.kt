package com.example.thedogapp.presentationlayer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.thedogapp.datalayer.TheDogApiRepository
import com.example.thedogapp.datalayer.toDogUiModel
import com.example.thedogapp.presentationlayer.ui.DogUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val ITEM_PER_PAGE = 25
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theDogApiRepository: TheDogApiRepository
) : ViewModel() {

    val items: Flow<PagingData<DogUiModel>> = Pager(
        config = PagingConfig(
            pageSize = ITEM_PER_PAGE,
            enablePlaceholders = false,
            initialLoadSize = ITEM_PER_PAGE
        ),
        pagingSourceFactory = {
            theDogApiRepository.getDogs()
        }
    )
        .flow
        .map { pagingData ->
            pagingData.map { dogData ->
                dogData.toDogUiModel()
            }
        }
        .cachedIn(viewModelScope)

    val itemsSorted: Flow<PagingData<DogUiModel>> = Pager(
        config = PagingConfig(
            pageSize = ITEM_PER_PAGE,
            enablePlaceholders = false,
            initialLoadSize = ITEM_PER_PAGE
        ),
        pagingSourceFactory = {
            theDogApiRepository.getDogsSorted()
        }
    )
        .flow
        .map { pagingData ->
            pagingData.map { dogData ->
                dogData.toDogUiModel()
            }
        }
        .cachedIn(viewModelScope)
}
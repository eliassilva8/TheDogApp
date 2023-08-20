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

private const val LIST_VIEW_LOAD_SIZE = 5
private const val GRID_VIEW_LOAD_SIZE = 12
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val theDogApiRepository: TheDogApiRepository
) : ViewModel() {

    var isListView = true
    fun getDogs(): Flow<PagingData<DogUiModel>> = Pager(
        config = PagingConfig(
            pageSize = loadSize(),
            enablePlaceholders = false,
            initialLoadSize = loadSize()
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

    fun getDogsSorted(): Flow<PagingData<DogUiModel>> = Pager(
        config = PagingConfig(
            pageSize = loadSize(),
            enablePlaceholders = false,
            initialLoadSize = loadSize()
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

    private fun loadSize() : Int {
        return if (isListView) LIST_VIEW_LOAD_SIZE else GRID_VIEW_LOAD_SIZE
    }
}
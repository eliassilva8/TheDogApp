package com.example.thedogapp.datalayer.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.thedogapp.datalayer.models.DogDataModel

abstract class BasePagingSource: PagingSource<Int, DogDataModel>() {
    override fun getRefreshKey(state: PagingState<Int, DogDataModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        return anchorPosition - state.config.pageSize / 2
    }

    fun getLoadResult(response: List<DogDataModel>, loadSize: Int, page: Int): LoadResult<Int, DogDataModel> {
        if (response.isEmpty()) {
            return LoadResult.Error(ApiEmptyResponseException("No dogs found"))
        } else {
        val hasMoreItems = response.size == loadSize
        val nextPage = if (hasMoreItems) {
            page + 1
        } else {
            null
        }

        return LoadResult.Page(
            data = response,
            prevKey = if (page == 0) null else page - 1,
            nextKey = nextPage
        )
    }
}
}
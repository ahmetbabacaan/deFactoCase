package com.babacan.defactocase.presentation.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.babacan.defactocase.common.AppConstants
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.domain.use_case.SearchMoviesUseCase
import javax.inject.Inject

class SearchPagingSourceFactory @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) {
    fun create(searchText: String): PagingSource<Int, Movie> {
        return object : PagingSource<Int, Movie>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

                if (searchText.length < AppConstants.SEARCH_MIN_CHAR_LENGTH) {
                    return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
                }

                val page = params.key ?: 1

                return searchMoviesUseCase(searchText, page).fold(
                    onSuccess = {
                        LoadResult.Page(
                            data = it,
                            prevKey = null,
                            nextKey = if (it.size < AppConstants.PAGE_SIZE) null else page + 1
                        )
                    },
                    onFailure = {
                        LoadResult.Error(it)
                    }
                )
            }

            override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val page = state.closestPageToPosition(anchorPosition)
                    page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
                }
            }
        }
    }
}
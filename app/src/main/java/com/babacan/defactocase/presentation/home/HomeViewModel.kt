package com.babacan.defactocase.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.babacan.defactocase.common.AppConstants
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.data.room.SearchResult
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.presentation.base.BaseViewModel
import com.babacan.defactocase.presentation.base.Effect
import com.babacan.defactocase.presentation.base.Event
import com.babacan.defactocase.presentation.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pagingSourceFactory: SearchPagingSourceFactory, private val deFactoDAO: DeFactoDAO
) : BaseViewModel<HomeEvent, HomeState, HomeEffect>() {
    override fun setInitialState(): HomeState {
        return HomeState()
    }

    init {
        updateFavorites()
    }

    private fun updateFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val favorites = deFactoDAO.getAllFavoriteMovies()
            setState { copy(favoriteItemList = favorites.toImmutableList()) }
        }
    }

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            HomeEvent.OnBackClicked -> {
                setEffect { HomeEffect.NavigateBack }
            }

            HomeEvent.OnDismissAddToFavoriteDialog -> {
                setState { copy(showAddToFavoriteDialog = Pair(false, null)) }
            }

            is HomeEvent.OnMovieDetailClicked -> {
                setEffect { HomeEffect.NavigateToMovieDetail(event.movieId) }
            }

            is HomeEvent.OnSearchTextChanged -> {
                setState { copy(searchText = event.query) }
                searchMovies(event.query)
            }

            is HomeEvent.OnMovieFavoriteClicked -> {
                val isFavorite = getCurrentState().favoriteItemList.map { it.imdbID }
                    .contains(event.movie.imdbID)

                if (isFavorite) {
                    setEvent(HomeEvent.OnMovieAddToFavorite(event.movie))
                } else {
                    setState { copy(showAddToFavoriteDialog = Pair(true, event.movie)) }
                }

            }

            is HomeEvent.OnMovieAddToFavorite -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val movie = event.movie
                    val isFavorite =
                        getCurrentState().favoriteItemList.map { it.imdbID }.contains(movie.imdbID)
                    if (isFavorite) {
                        deFactoDAO.deleteFavoriteMovie(movie.imdbID)
                    } else {
                        deFactoDAO.insertFavoriteMovie(movie)
                    }
                    updateFavorites()
                }
            }

            HomeEvent.OnFavoriteClicked -> {
                setEffect { HomeEffect.NavigateToFavoriteList }
            }

            HomeEvent.Init -> {
                updateFavorites()
                getSearchKeyword()
            }

            HomeEvent.OnFilterClicked -> {
                setState { copy(showFilterBottomSheet = true) }
            }

            HomeEvent.OnSortClicked -> {
                setState { copy(showSortBottomSheet = true) }
            }

            HomeEvent.OnDismissSortBottomSheet -> {
                setState { copy(showSortBottomSheet = false) }
            }

            HomeEvent.OnSortByTitle -> {
                setState { copy(showSortBottomSheet = false) }
            }
            HomeEvent.OnSortByYear -> {
                setState { copy(showSortBottomSheet = false) }
            }

            is HomeEvent.OnSearchDoneClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deFactoDAO.insertSearchResult(SearchResult(searchWord = event.searchText))
                    deFactoDAO.deleteOldSearchesKeepingLatest10()
                    getSearchKeyword()
                }
            }

            HomeEvent.OnSettingsClicked -> {
                setEffect { HomeEffect.NavigateToSettings }
            }
        }
    }

    private fun searchMovies(query: String) {
        val data = Pager(config = PagingConfig(
            pageSize = AppConstants.PAGE_SIZE, prefetchDistance = AppConstants.PREFETCH_DISTANCE
        ), pagingSourceFactory = { pagingSourceFactory.create(query) }).flow

        setState { copy(pagingDataFlow = data) }
    }

    private fun getSearchKeyword() {
        viewModelScope.launch(Dispatchers.IO) {
            deFactoDAO.getAllSearchResults().map { it.searchWord }
                .filter { it.isNotEmpty() }
                .let { searchResultList ->
                    setState { copy(searchResultList = searchResultList.toImmutableList()) }
                }
        }

    }
}

sealed interface HomeEvent : Event {
    data object Init : HomeEvent
    data object OnBackClicked : HomeEvent
    data object OnFavoriteClicked : HomeEvent
    data object OnDismissAddToFavoriteDialog : HomeEvent
    data object OnFilterClicked : HomeEvent
    data object OnSortClicked : HomeEvent
    data object OnDismissSortBottomSheet : HomeEvent
    data object OnSortByTitle : HomeEvent
    data object OnSortByYear : HomeEvent
    data object OnSettingsClicked : HomeEvent

    data class OnMovieDetailClicked(val movieId: String) : HomeEvent
    data class OnSearchTextChanged(val query: String) : HomeEvent
    data class OnMovieFavoriteClicked(val movie: Movie) : HomeEvent
    data class OnMovieAddToFavorite(val movie: Movie) : HomeEvent
    data class OnSearchDoneClicked(val searchText: String) : HomeEvent
}

data class HomeState(
    val isLoading: Boolean = false,
    val searchText: String = "",
    val favoriteItemList: ImmutableList<Movie> = persistentListOf(),
    val pagingDataFlow: Flow<PagingData<Movie>> = emptyFlow(),
    val showAddToFavoriteDialog: Pair<Boolean, Movie?> = Pair(false, null),
    val showSortBottomSheet: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val searchResultList: ImmutableList<String> = persistentListOf(),
) : State

sealed interface HomeEffect : Effect {
    data object NavigateBack : HomeEffect
    data object NavigateToFavoriteList : HomeEffect
    data object NavigateToSettings : HomeEffect
    data class NavigateToMovieDetail(val movieId: String) : HomeEffect
}
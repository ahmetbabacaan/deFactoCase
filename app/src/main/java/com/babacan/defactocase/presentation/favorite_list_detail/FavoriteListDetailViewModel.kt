package com.babacan.defactocase.presentation.favorite_list_detail

import androidx.lifecycle.viewModelScope
import com.babacan.defactocase.data.room.DeFactoDAO
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListDetailViewModel @Inject constructor(
    private val deFactoDAO: DeFactoDAO,
) :
    BaseViewModel<FavoriteListDetailEvent, FavoriteListDetailState, FavoriteListDetailEffect>() {
    override fun setInitialState(): FavoriteListDetailState {
        return FavoriteListDetailState()
    }

    override fun handleEvents(event: FavoriteListDetailEvent) {
        when (event) {
            FavoriteListDetailEvent.OnBackClicked -> {
                setEffect { FavoriteListDetailEffect.NavigateBack }
            }

            is FavoriteListDetailEvent.SetListName -> {
                setState { copy(listName = event.listName) }
                getFavoriteList()
            }

            is FavoriteListDetailEvent.OnMovieDetailClicked -> {

            }

            is FavoriteListDetailEvent.OnMovieFavoriteClicked -> {

            }

            is FavoriteListDetailEvent.OnMovieMoveFavoriteClicked -> {
                setState { copy(showMoveFavoriteDialog = Pair(true, event.movie)) }
            }

            FavoriteListDetailEvent.OnDismissMoveFavoriteDialog -> {
                setState { copy(showMoveFavoriteDialog = Pair(false, null)) }
            }

            is FavoriteListDetailEvent.OnMovieMoveFavoriteConfirmed -> {
                setState { copy(showMoveFavoriteDialog = Pair(false, null)) }
                event.previousItem?.let {
                    viewModelScope.launch(Dispatchers.IO) {
                        deFactoDAO.updateMoviesListName(
                            currentListName = event.currentName ?: "",
                            previousListName = it.listName,
                            movieId = it.imdbID
                        )
                    }
                    getFavoriteList()
                }
            }
        }
    }

    private fun getFavoriteList() {
        viewModelScope.launch(Dispatchers.IO) {
            deFactoDAO.getAllFavoriteMovies().apply {
                this.map { it.listName }.let {
                    setState { copy(favoriteList = it.distinct().toImmutableList()) }
                }
            }.filter {
                it.listName == getCurrentState().listName
            }.let { movies ->
                if (movies.isEmpty()) {
                   setEvent(FavoriteListDetailEvent.OnBackClicked)
                } else {
                    setState { copy(movies = movies.toImmutableList()) }
                }
            }
        }

    }
}

sealed interface FavoriteListDetailEvent : Event {
    data object OnBackClicked : FavoriteListDetailEvent
    data object OnDismissMoveFavoriteDialog : FavoriteListDetailEvent

    data class SetListName(val listName: String) : FavoriteListDetailEvent
    data class OnMovieDetailClicked(val imdbID: String) : FavoriteListDetailEvent
    data class OnMovieFavoriteClicked(val movie: Movie) : FavoriteListDetailEvent
    data class OnMovieMoveFavoriteClicked(val movie: Movie) : FavoriteListDetailEvent
    data class OnMovieMoveFavoriteConfirmed(val currentName: String?, val previousItem: Movie?) : FavoriteListDetailEvent
}

data class FavoriteListDetailState(
    val isLoading: Boolean = false,
    val listName: String = "",
    val movies: ImmutableList<Movie> = persistentListOf(),
    val showMoveFavoriteDialog: Pair<Boolean, Movie?> = Pair(false, null),
    val favoriteList: ImmutableList<String> = persistentListOf(),
) : State

sealed interface FavoriteListDetailEffect : Effect {
    data object NavigateBack : FavoriteListDetailEffect
}
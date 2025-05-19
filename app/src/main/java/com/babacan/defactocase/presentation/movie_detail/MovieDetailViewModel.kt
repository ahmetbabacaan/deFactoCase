package com.babacan.defactocase.presentation.movie_detail

import androidx.lifecycle.viewModelScope
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.domain.model.MovieDetails
import com.babacan.defactocase.domain.use_case.GetMovieDetailsUseCase
import com.babacan.defactocase.presentation.base.BaseViewModel
import com.babacan.defactocase.presentation.base.Effect
import com.babacan.defactocase.presentation.base.Event
import com.babacan.defactocase.presentation.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : BaseViewModel<MovieDetailEvent, MovieDetailState, MovieDetailEffect>() {
    override fun setInitialState(): MovieDetailState {
        return MovieDetailState()
    }

    override fun handleEvents(event: MovieDetailEvent) {
        when (event) {
            MovieDetailEvent.OnBackClicked -> {
                setEffect { MovieDetailEffect.NavigateBack }
            }

            MovieDetailEvent.OnRetryClicked -> {
                getMovieDetails(getCurrentState().movieId ?: "")
            }

            is MovieDetailEvent.SetMovieId -> {
                setState { copy(movieId = event.movieId) }
                getMovieDetails(event.movieId)
            }
        }
    }

    private fun getMovieDetails(movieId: String) {
        setState { copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            getMovieDetailsUseCase(movieId).onSuccess { movieDetail ->
                setState { copy(movieDetail = movieDetail, isLoading = false) }
            }.onFailure { error ->
                setState {
                    copy(
                        isError = true, errorMessage = error.message, isLoading = false
                    )
                }
            }
        }

    }
}

sealed interface MovieDetailEvent : Event {
    data object OnBackClicked : MovieDetailEvent
    data object OnRetryClicked : MovieDetailEvent
    data class SetMovieId(val movieId: String) : MovieDetailEvent
}

data class MovieDetailState(
    val movieId: String? = null,
    val isLoading: Boolean = false,
    val movieDetail: MovieDetails? = null,
    val isError: Boolean = false,
    val errorMessage: String? = null,
) : State

sealed interface MovieDetailEffect : Effect {
    data object NavigateBack : MovieDetailEffect
}
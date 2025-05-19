package com.babacan.defactocase.presentation.favorite_list

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
class FavoriteListViewModel @Inject constructor(
    private val deFactoDAO: DeFactoDAO
) :
    BaseViewModel<FavoriteListEvent, FavoriteListState, FavoriteListEffect>() {
    override fun setInitialState(): FavoriteListState {
        return FavoriteListState()
    }

    override fun handleEvents(event: FavoriteListEvent) {
        when (event) {
            FavoriteListEvent.OnBackClicked -> {
                setEffect { FavoriteListEffect.NavigateBack }
            }

            is FavoriteListEvent.OnFavoriteListClicked -> {
                setEffect { FavoriteListEffect.NavigateToFavoriteList(event.listName) }
            }

            is FavoriteListEvent.OnEditClicked -> {
                setState { copy(showEditDialog = Pair(true, event.listName)) }
            }

            is FavoriteListEvent.OnDeleteClicked -> {
                setState { copy(showDeleteDialog = Pair(true, event.listName)) }
            }

            FavoriteListEvent.OnDeleteCanceled -> {
                setState { copy(showDeleteDialog = Pair(false, null)) }
            }

            is FavoriteListEvent.OnDeleteConfirmed -> {
                setState { copy(showDeleteDialog = Pair(false, null)) }
                event.listName?.let { listName ->
                    viewModelScope.launch(Dispatchers.IO) {
                        deFactoDAO.deleteAllFavoriteMoviesByListName(listName)
                        getFavoriteList()
                    }
                }
            }

            FavoriteListEvent.OnEditCanceled -> {
                setState { copy(showEditDialog = Pair(false, null)) }
            }

            is FavoriteListEvent.OnEditConfirmed -> {
                setState { copy(showEditDialog = Pair(false, null)) }
                event.currentListName?.let { currentListName ->
                    viewModelScope.launch(Dispatchers.IO) {
                        deFactoDAO.updateFavoriteListName(
                            currentListName,
                            event.previousListName
                        )
                        getFavoriteList()
                    }
                }
            }
        }
    }

    init {
        getFavoriteList()
    }

    private fun getFavoriteList() {
        setState { copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            deFactoDAO.getAllFavoriteMovies().map { movies ->
                movies.listName
            }.distinct().let {
                if (it.isEmpty()) {
                    setState { copy(favoriteItemList = persistentListOf("Favoriler")) }
                } else {
                    setState { copy(favoriteItemList = it.toImmutableList()) }
                }
            }
        }
    }
}

sealed interface FavoriteListEvent : Event {
    data object OnBackClicked : FavoriteListEvent
    data object OnDeleteCanceled : FavoriteListEvent
    data object OnEditCanceled : FavoriteListEvent

    data class OnFavoriteListClicked(val listName: String): FavoriteListEvent
    data class OnEditClicked(val listName: String) : FavoriteListEvent
    data class OnDeleteClicked(val listName: String) : FavoriteListEvent
    data class OnDeleteConfirmed(val listName: String?) : FavoriteListEvent
    data class OnEditConfirmed(val currentListName: String?, val previousListName: String) : FavoriteListEvent
}

data class FavoriteListState(
    val isLoading: Boolean = false,
    val favoriteItemList: ImmutableList<String> = persistentListOf(),
    val showEditDialog: Pair<Boolean, String?> = Pair(false, null),
    val showDeleteDialog: Pair<Boolean, String?> = Pair(false, null),
) : State

sealed interface FavoriteListEffect : Effect {
    data object NavigateBack : FavoriteListEffect
    data class NavigateToFavoriteList(val listName: String): FavoriteListEffect
}
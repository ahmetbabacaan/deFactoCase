package com.babacan.defactocase.presentation.search

import com.babacan.defactocase.presentation.base.BaseViewModel
import com.babacan.defactocase.presentation.base.Effect
import com.babacan.defactocase.presentation.base.Event
import com.babacan.defactocase.presentation.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() :
    BaseViewModel<SearchEvent, SearchState, SearchEffect>() {
    override fun setInitialState(): SearchState {
        return SearchState()
    }

    override fun handleEvents(event: SearchEvent) {
        when (event) {
            SearchEvent.OnBackClicked -> {
                setEffect { SearchEffect.NavigateBack }
            }
        }
    }
}

sealed interface SearchEvent : Event {
    data object OnBackClicked : SearchEvent
}

data class SearchState(
    val isLoading: Boolean = false
) : State

sealed interface SearchEffect : Effect {
    data object NavigateBack : SearchEffect
}
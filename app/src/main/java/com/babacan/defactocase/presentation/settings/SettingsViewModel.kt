package com.babacan.defactocase.presentation.settings

import androidx.lifecycle.viewModelScope
import com.babacan.defactocase.data.PreferenceManager
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.presentation.base.BaseViewModel
import com.babacan.defactocase.presentation.base.Effect
import com.babacan.defactocase.presentation.base.Event
import com.babacan.defactocase.presentation.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferenceManager,
    private val deFactoDAO: DeFactoDAO,
) :
    BaseViewModel<SettingsEvent, SettingsState, SettingsEffect>() {
    override fun setInitialState(): SettingsState {
        return SettingsState()
    }

    init {
        val appVersion = javaClass.classLoader
            ?.getResource("META-INF/MANIFEST.MF")
            ?.readText()
            ?.split(" ")
            ?.firstOrNull { it.startsWith("Implementation-Version") }
            ?.split("=")
            ?.lastOrNull()

        setState { copy(appVersion = appVersion ?: "1.0") }
    }

    override fun handleEvents(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnBackClicked -> {
                setEffect { SettingsEffect.NavigateBack }
            }

            is SettingsEvent.OnDarkModeToggle -> {
                setEffect { SettingsEffect.SetDarkMode(event.darkMode) }
                viewModelScope.launch(Dispatchers.IO) {
                    prefs.setDarkMode(event.darkMode)
                }
                setState { copy(darkMode = event.darkMode) }
            }

            is SettingsEvent.Init -> {
                getDarkMode(isSystemLightMode = event.isSystemLightMode)
                viewModelScope.launch(Dispatchers.IO) {
                    deFactoDAO.getLoggedInUser().let { user ->
                        setState { copy(isUserLoggedIn = user?.isLoggedIn == true) }
                    }
                }
            }

            is SettingsEvent.OnLanguageSelected -> {
            }

            SettingsEvent.OnChangePasswordClicked -> {
                setEffect { SettingsEffect.NavigateToChangePassword }
            }

            SettingsEvent.OnLoginClicked -> {
                setEffect { SettingsEffect.NavigateToLogin }
            }

            SettingsEvent.OnRegisterClicked -> {
                setEffect { SettingsEffect.NavigateToRegister }
            }

            SettingsEvent.OnLogoutClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deFactoDAO.getLoggedInUser()?.let { user ->
                        deFactoDAO.updateLoggedInStatus(user._id, false)
                        setState { copy(isUserLoggedIn = false) }
                    }
                }
            }
        }
    }

    private fun getDarkMode(isSystemLightMode: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.isDarkMode
                .collect { darkMode ->
                    if (darkMode == null) {
                        setState { copy(darkMode = isSystemLightMode.not()) }
                    } else {
                        setState { copy(darkMode = darkMode) }
                    }
                }
        }
    }
}

sealed interface SettingsEvent : Event {
    data object OnBackClicked : SettingsEvent
    data object OnRegisterClicked : SettingsEvent
    data object OnLoginClicked : SettingsEvent
    data object OnChangePasswordClicked : SettingsEvent
    data object OnLogoutClicked : SettingsEvent

    data class OnDarkModeToggle(val darkMode: Boolean) : SettingsEvent
    data class Init(val isSystemLightMode: Boolean) : SettingsEvent
    data class OnLanguageSelected(val language: String) : SettingsEvent
}

data class SettingsState(
    val isLoading: Boolean = false,
    val appVersion: String = "",
    val darkMode: Boolean = false,
    val isUserLoggedIn: Boolean = false,
) : State

sealed interface SettingsEffect : Effect {
    data object NavigateBack : SettingsEffect
    data object NavigateToLogin : SettingsEffect
    data object NavigateToRegister : SettingsEffect
    data object NavigateToChangePassword : SettingsEffect
    data class SetDarkMode(val darkMode: Boolean) : SettingsEffect
}
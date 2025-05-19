package com.babacan.defactocase.presentation.login

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.babacan.defactocase.common.toSHA1
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
class LoginViewModel @Inject constructor(
    private val deFactoDAO: DeFactoDAO,
) : BaseViewModel<LoginEvent, LoginState, LoginEffect>() {
    override fun setInitialState(): LoginState {
        return LoginState()
    }

    override fun handleEvents(event: LoginEvent) {
        when (event) {
            LoginEvent.OnBackClicked -> {
                setEffect { LoginEffect.NavigateBack }
            }

            is LoginEvent.OnEmailChanged -> {
                setState { copy(email = event.email) }
            }

            is LoginEvent.OnPasswordChange -> {
                setState { copy(password = event.password) }
            }

            LoginEvent.OnPasswordVisibilityChanged -> {
                setState { copy(showPasswordVisibility = !showPasswordVisibility) }
            }

            LoginEvent.OnLoginClicked -> {
                if (state.value.isButtonEnabled) {
                    viewModelScope.launch(Dispatchers.IO) {
                        deFactoDAO.checkUser(
                            email = state.value.email,
                            password = state.value.password.toSHA1()
                        ).let {
                            if (it != null) {
                                deFactoDAO.updateLoggedInStatus(it._id, true)
                                setEffect { LoginEffect.NavigateBack }
                            } else {
                                setState { copy(showError = true) }
                            }
                        }
                    }
                }
            }

            LoginEvent.DisableErrorMessage -> {
                setState { copy(showError = false) }
            }
        }
    }
}

sealed interface LoginEvent : Event {
    data object OnBackClicked : LoginEvent
    data object OnLoginClicked : LoginEvent
    data object DisableErrorMessage : LoginEvent
    data object OnPasswordVisibilityChanged : LoginEvent

    data class OnPasswordChange(val password: String) : LoginEvent
    data class OnEmailChanged(val email: String) : LoginEvent
}

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val showPasswordVisibility: Boolean = true,
    val showError: Boolean = false,
) : State {
    private val isEmailValid: Boolean
        get() = email.isNotEmpty()

    private val isPasswordValid: Boolean
        get() = password.isNotEmpty()

    val passwordVisualTransformation: VisualTransformation
        get() = if (showPasswordVisibility) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }

    val isButtonEnabled: Boolean
        get() = isEmailValid && isPasswordValid
}

sealed interface LoginEffect : Effect {
    data object NavigateBack : LoginEffect
}
package com.babacan.defactocase.presentation.reset_password

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.babacan.defactocase.R
import com.babacan.defactocase.common.toSHA1
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.common.StringProvider
import com.babacan.defactocase.domain.model.PasswordConstraint
import com.babacan.defactocase.domain.model.PasswordConstraintType
import com.babacan.defactocase.presentation.base.BaseViewModel
import com.babacan.defactocase.presentation.base.Effect
import com.babacan.defactocase.presentation.base.Event
import com.babacan.defactocase.presentation.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val deFactoDAO: DeFactoDAO,
    stringProvider: StringProvider,
) : BaseViewModel<ResetPasswordEvent, ResetPasswordState, ResetPasswordEffect>() {

    override fun setInitialState(): ResetPasswordState = ResetPasswordState()

    init {
        val constraints = listOf(
            PasswordConstraint(
                PasswordConstraintType.LENGTH,
                stringProvider.getString(R.string.password_constraint_message),
                false
            ),
            PasswordConstraint(
                PasswordConstraintType.UPPER_CASE,
                stringProvider.getString(R.string.password_constraint_message_2),
                false
            ),
            PasswordConstraint(
                PasswordConstraintType.LOWER_CASE,
                stringProvider.getString(R.string.password_constraint_message_3),
                false
            ),
            PasswordConstraint(
                PasswordConstraintType.NUMBER_SPECIAL_CHARACTER,
                stringProvider.getString(R.string.password_constraint_message_4),
                false
            ),
        )
        setState { copy(passwordConstraints = constraints) }
    }

    override fun handleEvents(event: ResetPasswordEvent) {
        when (event) {
            ResetPasswordEvent.OnBackClicked -> {
                setEffect { ResetPasswordEffect.NavigateBack }
            }

            ResetPasswordEvent.OnPasswordVisibilityChanged -> {
                setState { copy(showPasswordVisibility = !showPasswordVisibility) }
            }

            is ResetPasswordEvent.OnNewPasswordChanged -> {
                setState {
                    copy(
                        newPassword = event.password,
                        passwordConstraints = passwordConstraints.map { constraint ->
                            constraint.copy(isValid = constraint.id.predicate(event.password))
                        }
                    )
                }
            }

            is ResetPasswordEvent.OnPreviousPasswordChanged -> {
                setState { copy(previousPassword = event.password) }
            }

            ResetPasswordEvent.OnRegisterClicked -> {
                setState { copy(isLoading = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    val user = deFactoDAO.getLoggedInUser()
                    if (user != null) {
                        val checkPassword = deFactoDAO.checkPassword(
                            password = getCurrentState().previousPassword.toSHA1(),
                            userId = user._id
                        )
                        if (checkPassword == null) {
                            setState { copy(isError = true, isLoading = false) }
                            return@launch
                        }
                        deFactoDAO.updateUserPassword(
                            user._id,
                            getCurrentState().newPassword.toSHA1()
                        )
                        setEffect { ResetPasswordEffect.NavigateBack }
                    }
                }
            }

            ResetPasswordEvent.OnDismissErrorMessage -> {
                setState { copy(isError = false) }
            }
        }
    }
}

sealed interface ResetPasswordEvent : Event {
    data object OnBackClicked : ResetPasswordEvent
    data object OnPasswordVisibilityChanged : ResetPasswordEvent
    data object OnRegisterClicked : ResetPasswordEvent
    data object OnDismissErrorMessage : ResetPasswordEvent

    data class OnPreviousPasswordChanged(val password: String) : ResetPasswordEvent
    data class OnNewPasswordChanged(val password: String) : ResetPasswordEvent
}

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val previousPassword: String = "",
    val newPassword: String = "",
    val showPasswordVisibility: Boolean = true,
    val isError: Boolean = false,
    val passwordConstraints: List<PasswordConstraint> = emptyList(),
) : State {
    val passwordVisualTransformation: VisualTransformation
        get() = if (showPasswordVisibility) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }

    val isButtonEnabled: Boolean
        get() = passwordConstraints.all { it.isValid }
}

sealed interface ResetPasswordEffect : Effect {
    data object NavigateBack : ResetPasswordEffect
}
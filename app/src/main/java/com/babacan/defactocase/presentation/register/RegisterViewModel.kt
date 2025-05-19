package com.babacan.defactocase.presentation.register

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.babacan.defactocase.R
import com.babacan.defactocase.common.toSHA1
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.common.StringProvider
import com.babacan.defactocase.domain.model.PasswordConstraint
import com.babacan.defactocase.domain.model.PasswordConstraintType
import com.babacan.defactocase.domain.model.User
import com.babacan.defactocase.presentation.base.BaseViewModel
import com.babacan.defactocase.presentation.base.Effect
import com.babacan.defactocase.presentation.base.Event
import com.babacan.defactocase.presentation.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val deFactoDAO: DeFactoDAO,
    stringProvider: StringProvider,
) : BaseViewModel<RegisterEvent, RegisterState, RegisterEffect>() {

    override fun setInitialState(): RegisterState {
        return RegisterState()
    }

    override fun handleEvents(event: RegisterEvent) {
        when (event) {
            RegisterEvent.OnBackClicked -> {
                setEffect { RegisterEffect.NavigateBack }
            }

            is RegisterEvent.OnEmailChanged -> {
                setState { copy(email = event.email) }
            }

            is RegisterEvent.OnFirstPasswordChanged -> {
                setState {
                    copy(
                        firstPassword = event.password,
                        passwordConstraints = passwordConstraints.map { constraint ->
                            constraint.copy(isValid = constraint.id.predicate(event.password))
                        },
                        secondPasswordMatch = secondPassword == event.password
                    )
                }
            }

            is RegisterEvent.OnSecondPasswordChanged -> {
                setState {
                    copy(
                        secondPassword = event.password,
                        secondPasswordMatch = firstPassword == event.password
                    )
                }
            }

            RegisterEvent.OnPasswordVisibilityChanged -> {
                setState {
                    copy(showPasswordVisibility = !showPasswordVisibility)
                }
            }

            RegisterEvent.OnRegisterClicked -> {
                if (getCurrentState().email.isNotEmpty() && getCurrentState().firstPassword.isNotEmpty()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        deFactoDAO.insertUser(
                            User(
                                email = getCurrentState().email,
                                password = getCurrentState().firstPassword.toSHA1(),
                                isLoggedIn = true
                            )
                        )
                        setEffect { RegisterEffect.NavigateBack }
                    }
                }
            }
        }
    }

    init {
        val constraints = listOf(
            PasswordConstraint(
                PasswordConstraintType.MAIL,
                stringProvider.getString(R.string.email_constraint_message),
                false
            ),
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
            PasswordConstraint(
                PasswordConstraintType.MATCH_WITH_FIRST,
                stringProvider.getString(R.string.password_constraint_message_5),
                false
            ),
        )

        setState {
            copy(
                passwordConstraints = constraints,
            )
        }
    }
}

sealed interface RegisterEvent : Event {
    data object OnBackClicked : RegisterEvent
    data object OnPasswordVisibilityChanged : RegisterEvent
    data object OnRegisterClicked : RegisterEvent

    data class OnEmailChanged(val email: String) : RegisterEvent
    data class OnFirstPasswordChanged(val password: String) : RegisterEvent
    data class OnSecondPasswordChanged(val password: String) : RegisterEvent
}

data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val showPasswordVisibility: Boolean = true,
    val firstPassword: String = "",
    val secondPassword: String = "",
    val passwordConstraints: List<PasswordConstraint> = emptyList(),
    val secondPasswordMatch: Boolean = false,
) : State {
    val isEmailError: Boolean
        get() = passwordConstraints.first { it.id == PasswordConstraintType.MAIL }.id.predicate(
            email
        )

    val passwordVisualTransformation: VisualTransformation
        get() = if (showPasswordVisibility) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }

    val isButtonEnabled: Boolean
        get() = passwordConstraints.all {
            when (it.id) {
                PasswordConstraintType.MAIL -> {
                    isEmailError
                }

                PasswordConstraintType.MATCH_WITH_FIRST -> {
                    secondPasswordMatch
                }

                else -> {
                    it.isValid
                }
            }
        } && secondPasswordMatch
}

sealed interface RegisterEffect : Effect {
    data object NavigateBack : RegisterEffect
}
package com.babacan.defactocase.presentation.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.babacan.defactocase.R
import com.babacan.defactocase.domain.model.PasswordConstraintType
import com.babacan.defactocase.presentation.register.PasswordConstraint
import com.babacan.defactocase.presentation.register.RegisterEvent
import com.babacan.defactocase.ui.drawables.IcEyesOff
import com.babacan.defactocase.ui.drawables.IcEyesOn
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    navigateBack: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateBack -> navigateBack()
            }
        }
    }

    LoginScreen(
        viewState = viewState,
        onViewEvent = viewModel::setEvent
    )
}

@Composable
fun LoginScreen(
    viewState: LoginState,
    onViewEvent: (LoginEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(16.dp),
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier
                            .padding(16.dp),
                        action = {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = Color.White,
                                modifier = Modifier.clickable { onViewEvent(LoginEvent.DisableErrorMessage) }
                            )
                        },
                        contentColor = Color.White,
                        containerColor = Color.Red,
                        content = { Text(text = data.visuals.message) }
                    )
                }
            )
        },
        topBar = {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp)
                    .clickable { onViewEvent(LoginEvent.OnBackClicked) },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        },
    ) { innerPadding ->
        val coroutine = rememberCoroutineScope()
        val errorMessage = stringResource(id = R.string.email_or_password_is_incorrect)
        val dismissLabel = stringResource(id = R.string.dismiss)

        LaunchedEffect(viewState.showError) {
            if (viewState.showError) {
                coroutine.launch {
                    snackBarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = dismissLabel,
                        duration = SnackbarDuration.Short
                    )
                }
                onViewEvent(LoginEvent.DisableErrorMessage)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = viewState.email,
                onValueChange = { onViewEvent(LoginEvent.OnEmailChanged(it)) },
                label = { Text(stringResource(id = R.string.email)) },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
            )

            TextField(
                value = viewState.password,
                onValueChange = { onViewEvent(LoginEvent.OnPasswordChange(it)) },
                label = { Text(stringResource(id = R.string.password)) },
                visualTransformation = viewState.passwordVisualTransformation,
                trailingIcon = {
                    Icon(
                        imageVector = if (viewState.showPasswordVisibility) Icons.IcEyesOff else Icons.IcEyesOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onViewEvent(LoginEvent.OnPasswordVisibilityChanged) }
                    )
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
            )


            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onViewEvent(LoginEvent.OnLoginClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewState.isButtonEnabled
            ) {
                Text(text = stringResource(id = R.string.login))
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        viewState = LoginState(),
        onViewEvent = { }
    )
}

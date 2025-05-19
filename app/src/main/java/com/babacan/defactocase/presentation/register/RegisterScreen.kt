package com.babacan.defactocase.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babacan.defactocase.domain.model.PasswordConstraint
import com.babacan.defactocase.domain.model.PasswordConstraintType
import com.babacan.defactocase.ui.drawables.IcCheckError
import com.babacan.defactocase.ui.drawables.IcCheckSuccess
import com.babacan.defactocase.ui.drawables.IcEyesOff
import com.babacan.defactocase.ui.drawables.IcEyesOn
import com.babacan.defactocase.R

@Composable
fun RegisterRoute(
    navigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RegisterEffect.NavigateBack -> navigateBack()
            }
        }
    }

    RegisterScreen(
        viewState = viewState,
        onViewEvent = viewModel::setEvent
    )

}

@Composable
fun RegisterScreen(
    viewState: RegisterState,
    onViewEvent: (RegisterEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp)
                    .clickable { onViewEvent(RegisterEvent.OnBackClicked) },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.create_account),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = viewState.email,
                onValueChange = { onViewEvent(RegisterEvent.OnEmailChanged(it)) },
                label = { Text(stringResource(id = R.string.email)) },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
            )

            TextField(
                value = viewState.firstPassword,
                onValueChange = { onViewEvent(RegisterEvent.OnFirstPasswordChanged(it)) },
                label = { Text(stringResource(id = R.string.password)) },
                visualTransformation = viewState.passwordVisualTransformation,
                trailingIcon = {
                    Icon(
                        imageVector = if (viewState.showPasswordVisibility) Icons.IcEyesOff else Icons.IcEyesOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onViewEvent(RegisterEvent.OnPasswordVisibilityChanged) }
                    )
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
            )

            TextField(
                value = viewState.secondPassword,
                onValueChange = { onViewEvent(RegisterEvent.OnSecondPasswordChanged(it)) },
                label = { Text(stringResource(id = R.string.confirm_password)) },
                visualTransformation = viewState.passwordVisualTransformation,
                trailingIcon = {
                    Icon(
                        imageVector = if (viewState.showPasswordVisibility) Icons.IcEyesOff else Icons.IcEyesOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onViewEvent(RegisterEvent.OnPasswordVisibilityChanged) }
                    )
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
            )
            viewState.passwordConstraints.forEach {
                when (it.id) {
                    PasswordConstraintType.MAIL -> {
                        val constraint = it.copy(
                            isValid = viewState.isEmailError,
                        )
                        PasswordConstraint(constraint = constraint)
                    }

                    PasswordConstraintType.MATCH_WITH_FIRST -> {
                        val constraint = it.copy(
                            isValid = viewState.secondPasswordMatch,
                        )
                        PasswordConstraint(constraint = constraint)
                    }

                    else -> {
                        PasswordConstraint(constraint = it)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onViewEvent(RegisterEvent.OnRegisterClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewState.isButtonEnabled
            ) {
                Text(text = stringResource(id = R.string.register))
            }
        }
    }
}

@Composable
fun PasswordConstraint(modifier: Modifier = Modifier, constraint: PasswordConstraint) {
    Row {
        Image(
            imageVector = if (constraint.isValid) Icons.IcCheckSuccess else Icons.IcCheckError,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = constraint.text,
            style = MaterialTheme.typography.labelSmall,
            modifier = modifier
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(
        viewState = RegisterState(),
        onViewEvent = { }
    )
}

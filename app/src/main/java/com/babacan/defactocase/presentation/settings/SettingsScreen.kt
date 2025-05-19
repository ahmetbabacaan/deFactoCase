package com.babacan.defactocase.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babacan.defactocase.R
import com.babacan.defactocase.common.LocaleHelper

@Composable
fun SettingsRoute(
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToResetPassword: () -> Unit,
    isDarkMode: (Boolean) -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val isSystemLightMode = MaterialTheme.colorScheme.isLight()
    viewModel.setEvent(SettingsEvent.Init(isSystemLightMode))

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsEffect.NavigateBack -> navigateBack()
                is SettingsEffect.SetDarkMode -> {
                    isDarkMode(effect.darkMode)
                }

                SettingsEffect.NavigateToChangePassword -> {
                    navigateToResetPassword()
                }

                SettingsEffect.NavigateToLogin -> {
                    navigateToLogin()
                }

                SettingsEffect.NavigateToRegister -> {
                    navigateToRegister()
                }
            }
        }
    }

    SettingsScreen(
        viewState = viewState, onViewEvent = viewModel::setEvent
    )

}

@Composable
fun ColorScheme.isLight(): Boolean = this.background.luminance() > 0.5

@Composable
fun SettingsScreen(
    viewState: SettingsState, onViewEvent: (SettingsEvent) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp)
                    .clickable { onViewEvent(SettingsEvent.OnBackClicked) },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )


        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
            )

            DarkModeToggle(viewState.darkMode) {
                onViewEvent(SettingsEvent.OnDarkModeToggle(it))
            }

            LanguageSelector(
                modifier = Modifier,
                selectedLangCode = LocaleHelper.getLangCode(context),
                onEnglishSelected = {
                    LocaleHelper.updateLocale(
                        context = context,
                        languageCode = "en"
                    )
                },
                onTurkishSelected = {
                    LocaleHelper.updateLocale(
                        context = context,
                        languageCode = "tr"
                    )
                }
            )

            AuthenticationSettings(
                modifier = Modifier.padding(top = 8.dp),
                isUserLoggedIn = viewState.isUserLoggedIn,
                onLoginClicked = { onViewEvent(SettingsEvent.OnLoginClicked) },
                onRegisterClicked = { onViewEvent(SettingsEvent.OnRegisterClicked) },
                onResetPasswordClicked = { onViewEvent(SettingsEvent.OnChangePasswordClicked) }
            )

            Spacer(modifier = Modifier.weight(1f))

            if (viewState.isUserLoggedIn) {
                Button(
                    onClick = { onViewEvent(SettingsEvent.OnLogoutClicked) },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(MaterialTheme.shapes.small),
                ) {
                    Text(text = stringResource(id = R.string.logout))
                }
            }

            Text(
                text = stringResource(id = R.string.app_version, viewState.appVersion),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

    }
}

@Composable
fun DarkModeToggle(
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    onToggle: (Boolean) -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.dark_mode), modifier = Modifier.weight(1f))
            Switch(
                modifier = Modifier,
                checked = isDarkMode,
                onCheckedChange = { onToggle(it) },
            )
        }
    }
}

@Composable
fun LanguageSelector(
    modifier: Modifier = Modifier,
    selectedLangCode: String = "en",
    onEnglishSelected: () -> Unit = {},
    onTurkishSelected: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.language), modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onEnglishSelected()
                },
                modifier = Modifier
                    .padding(4.dp),
                enabled = selectedLangCode != "en"
            ) {
                Text(stringResource(id = R.string.language_english))
            }

            Button(
                onClick = {
                    onTurkishSelected()
                },
                modifier = Modifier
                    .padding(4.dp),
                enabled = selectedLangCode != "tr"
            ) {
                Text(stringResource(id = R.string.language_turkish))
            }
        }
    }
}

@Composable
fun AuthenticationSettings(
    modifier: Modifier = Modifier,
    isUserLoggedIn: Boolean = false,
    onLoginClicked: () -> Unit = {},
    onRegisterClicked: () -> Unit = {},
    onResetPasswordClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.authentication_setting),
                modifier = Modifier.weight(1f)
            )
            if (isUserLoggedIn) {
                Button(
                    onClick = { onResetPasswordClicked() },
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(MaterialTheme.shapes.small)
                ) {
                    Text(stringResource(id = R.string.password_change))
                }

            } else {
                Button(
                    onClick = { onLoginClicked() },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .padding(end = 4.dp)
                ) {
                    Text(stringResource(id = R.string.login))
                }

                Button(
                    onClick = { onRegisterClicked() },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)

                ) {
                    Text(stringResource(id = R.string.register))
                }
            }
        }
    }
}

@Preview
@Composable
private fun AuthenticationSettingsPreview() {
    AuthenticationSettings()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        viewState = SettingsState(
            appVersion = "1.0",
        ), onViewEvent = { })
}

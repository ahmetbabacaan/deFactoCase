package com.babacan.defactocase.presentation.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SettingsRoute = "SettingsRoute"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SettingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToResetPassword: () -> Unit,
    isDarkMode: (Boolean) -> Unit = {},
) {
    composable(
        route = SettingsRoute,
    ) {
        SettingsRoute(
            navigateBack = navigateBack,
            isDarkMode = isDarkMode,
            navigateToLogin = navigateToLogin,
            navigateToRegister = navigateToRegister,
            navigateToResetPassword = navigateToResetPassword
        )
    }
}

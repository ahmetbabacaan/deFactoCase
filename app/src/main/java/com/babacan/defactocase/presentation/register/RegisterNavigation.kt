package com.babacan.defactocase.presentation.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val RegisterRoute = "RegisterRoute"

fun NavController.navigateToRegister(navOptions: NavOptions? = null) {
    this.navigate(RegisterRoute, navOptions)
}

fun NavGraphBuilder.registerScreen(
    navigateBack: () -> Unit,
) {
    composable(
        route = RegisterRoute,
    ) {
        RegisterRoute(
            navigateBack = navigateBack,
        )
    }
}

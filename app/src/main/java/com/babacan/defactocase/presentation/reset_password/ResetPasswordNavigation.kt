package com.babacan.defactocase.presentation.reset_password

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ResetPasswordRoute = "ResetPasswordRoute"

fun NavController.navigateToResetPassword(navOptions: NavOptions? = null) {
    this.navigate(ResetPasswordRoute, navOptions)
}

fun NavGraphBuilder.resetPasswordScreen(
    navigateBack: () -> Unit,
) {
    composable(
        route = ResetPasswordRoute,
    ) {
        ResetPasswordRoute(
            navigateBack = navigateBack,
        )
    }
}

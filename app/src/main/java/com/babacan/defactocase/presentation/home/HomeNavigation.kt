package com.babacan.defactocase.presentation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HomeRoute = "HomeRoute"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    navigateBack: () -> Unit,
    navigateSettings: () -> Unit,
    navigateFavoriteList: () -> Unit,
    navigateToMovieDetail: (String) -> Unit,
) {
    composable(
        route = HomeRoute,
    ) {
        HomeRoute(
            navigateBack = navigateBack,
            navigateFavoriteList = navigateFavoriteList,
            navigateToMovieDetail = navigateToMovieDetail,
            navigateSettings = navigateSettings,
        )
    }
}

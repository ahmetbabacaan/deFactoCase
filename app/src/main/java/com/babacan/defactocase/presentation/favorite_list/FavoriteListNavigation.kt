package com.babacan.defactocase.presentation.favorite_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val FavoriteListNavigationRoute = "FavoriteListNavigationRoute"

fun NavController.navigateToFavoriteList(navOptions: NavOptions? = null) {
    this.navigate(FavoriteListNavigationRoute, navOptions)
}

fun NavGraphBuilder.favoriteListScreen(
    navigateBack: () -> Unit,
    navigateToFavoriteListDetail: (String) -> Unit,
) {
    composable(
        route = FavoriteListNavigationRoute,
    ) {
        FavoriteListRoute(
            navigateBack = navigateBack,
            navigateToFavoriteListDetail = navigateToFavoriteListDetail,
        )
    }
}

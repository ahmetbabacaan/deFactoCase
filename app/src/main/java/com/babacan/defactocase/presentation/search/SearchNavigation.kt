package com.babacan.defactocase.presentation.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SearchRoute = "SearchRoute"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(SearchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    navigateBack: () -> Unit,
) {
    composable(
        route = SearchRoute,
    ) {
        SearchRoute(
            navigateBack = navigateBack,
        )
    }
}

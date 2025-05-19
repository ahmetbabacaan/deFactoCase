package com.babacan.defactocase.presentation.favorite_list_detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val FavoriteListDetailRoute = "FavoriteListDetailRoute"
const val FavoriteListDetailRouteWithArgs = "$FavoriteListDetailRoute?listName={listName}"


fun NavController.navigateToFavoriteListDetail(
    listName: String,
    navOptions: NavOptions? = null
) {
    this.navigate("$FavoriteListDetailRoute?listName=$listName", navOptions)
}

fun NavGraphBuilder.favoriteListDetailScreen(
    navigateBack: () -> Unit,
) {
    composable(
        route = FavoriteListDetailRouteWithArgs,
        arguments = listOf(
            navArgument("listName") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) { backStackEntry ->
        val listName = backStackEntry.arguments?.getString("listName") ?: ""

        FavoriteListDetailRoute(
            listName = listName,
            navigateBack = navigateBack
        )
    }
}

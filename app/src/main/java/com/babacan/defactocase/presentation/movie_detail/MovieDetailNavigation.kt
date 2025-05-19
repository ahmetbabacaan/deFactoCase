package com.babacan.defactocase.presentation.movie_detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val MovieDetailRoute = "MovieDetailRoute"
const val MovieDetailRouteWithArgs = "$MovieDetailRoute?movieId={movieId}"

fun NavController.navigateToMovieDetail(navOptions: NavOptions? = null, movieId: String) {
    this.navigate("$MovieDetailRoute?movieId=$movieId", navOptions)
}

fun NavGraphBuilder.movieDetailScreen(
    navigateBack: () -> Unit,
) {
    composable(
        route = MovieDetailRouteWithArgs,
        arguments = listOf(
            navArgument("movieId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) { backStackEntry ->
        val movieId = backStackEntry.arguments?.getString("movieId") ?: ""

        MovieDetailRoute(
            navigateBack = navigateBack,
            movieId = movieId
        )
    }
}


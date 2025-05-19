package com.babacan.defactocase.navigation

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.babacan.defactocase.presentation.favorite_list.favoriteListScreen
import com.babacan.defactocase.presentation.favorite_list.navigateToFavoriteList
import com.babacan.defactocase.presentation.favorite_list_detail.favoriteListDetailScreen
import com.babacan.defactocase.presentation.favorite_list_detail.navigateToFavoriteListDetail
import com.babacan.defactocase.presentation.home.homeScreen
import com.babacan.defactocase.presentation.login.loginScreen
import com.babacan.defactocase.presentation.login.navigateToLogin
import com.babacan.defactocase.presentation.movie_detail.movieDetailScreen
import com.babacan.defactocase.presentation.movie_detail.navigateToMovieDetail
import com.babacan.defactocase.presentation.register.navigateToRegister
import com.babacan.defactocase.presentation.register.registerScreen
import com.babacan.defactocase.presentation.reset_password.navigateToResetPassword
import com.babacan.defactocase.presentation.reset_password.resetPasswordScreen
import com.babacan.defactocase.presentation.search.searchScreen
import com.babacan.defactocase.presentation.settings.navigateToSettings
import com.babacan.defactocase.presentation.settings.settingsScreen

@Composable
fun DeFactoNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    isDarkMode: (Boolean) -> Unit = {},
) {
    NavHost(
        modifier = modifier.statusBarsPadding(),
        navController = navController,
        startDestination = startDestination,
    ) {

        homeScreen(
            navigateBack = { navController.popBackStack() },
            navigateToMovieDetail = {
                navController.navigateToMovieDetail(
                    navOptions = null, movieId = it
                )
            },
            navigateFavoriteList = {
                navController.navigateToFavoriteList()
            },
            navigateSettings = {
                navController.navigateToSettings()
            },
        )

        favoriteListScreen(
            navigateBack = { navController.popBackStack() },
            navigateToFavoriteListDetail = {
                navController.navigateToFavoriteListDetail(
                    listName = it
                )
            },
        )

        favoriteListDetailScreen(
            navigateBack = { navController.popBackStack() },
        )

        movieDetailScreen(
            navigateBack = { navController.popBackStack() },
        )

        searchScreen(
            navigateBack = { navController.popBackStack() },
        )

        settingsScreen(
            navigateBack = { navController.popBackStack() },
            navigateToLogin = { navController.navigateToLogin() },
            navigateToRegister = { navController.navigateToRegister() },
            navigateToResetPassword = { navController.navigateToResetPassword() },
            isDarkMode = isDarkMode,
        )

        loginScreen(
            navigateBack = { navController.popBackStack() },
        )

        registerScreen(
            navigateBack = { navController.popBackStack() },
        )

        resetPasswordScreen(
            navigateBack = { navController.popBackStack() },
        )
    }
}
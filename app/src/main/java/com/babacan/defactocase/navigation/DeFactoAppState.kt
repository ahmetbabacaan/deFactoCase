package com.babacan.defactocase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.babacan.defactocase.presentation.home.HomeRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberDeFactoAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): DeFactoAppState {
    return remember(navController, coroutineScope) {
        DeFactoAppState(navController)
    }
}

@Stable
class DeFactoAppState(
    val navController: NavHostController,
) {

    val startDestination: String = HomeRoute

}

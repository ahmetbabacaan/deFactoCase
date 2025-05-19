package com.babacan.defactocase

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.babacan.defactocase.navigation.DeFactoAppState
import com.babacan.defactocase.navigation.DeFactoNavHost
import com.babacan.defactocase.navigation.rememberDeFactoAppState

@Composable
fun DeFactoApp(
    appState: DeFactoAppState = rememberDeFactoAppState(),
    isDarkMode: (Boolean) -> Unit = {},
) {
    DeFactoNavHost(
        navController = appState.navController,
        startDestination = appState.startDestination,
        modifier = Modifier,
        isDarkMode = isDarkMode,
    )
}
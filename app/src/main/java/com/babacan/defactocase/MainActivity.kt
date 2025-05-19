package com.babacan.defactocase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.babacan.defactocase.presentation.settings.isLight
import com.babacan.defactocase.ui.theme.DeFactoCaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isSystemLightMode = MaterialTheme.colorScheme.isLight()
            val toggleDarkMode: MutableState<Boolean?> = remember { mutableStateOf(null) }
            DeFactoCaseTheme (
                darkTheme = toggleDarkMode.value ?: viewModel.darkMode.collectAsState().value ?: isSystemLightMode.not(),
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                ) { innerPadding ->
                    DeFactoApp(
                        isDarkMode = {
                            toggleDarkMode.value = it
                        },
                    )
                }
            }
        }
    }
}

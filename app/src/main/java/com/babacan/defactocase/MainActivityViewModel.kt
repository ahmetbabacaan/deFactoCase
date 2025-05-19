package com.babacan.defactocase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babacan.defactocase.data.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val prefs: PreferenceManager
): ViewModel() {

    private val _darkMode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val darkMode = _darkMode.asStateFlow()

    private fun getDarkMode() {
        viewModelScope.launch {
            prefs.isDarkMode.collect { darkMode ->
                _darkMode.value = darkMode
            }
        }
    }

    init {
        getDarkMode()
    }
}
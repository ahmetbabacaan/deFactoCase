package com.babacan.defactocase.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.settings by preferencesDataStore(name = "settings")

class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")
    }

    val isDarkMode: Flow<Boolean?> = context.settings.data
        .map { preferences ->
            preferences[DARK_MODE_KEY]
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settings.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }
}

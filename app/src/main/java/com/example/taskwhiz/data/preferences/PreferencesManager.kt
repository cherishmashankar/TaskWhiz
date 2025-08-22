package com.example.taskwhiz.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Use constant instead of hardcoding "settings"
val Context.dataStore by preferencesDataStore(name = PreferencesConstants.DATASTORE_NAME)

object PreferencesKeys {
    val LANGUAGE = stringPreferencesKey(PreferencesConstants.KEY_LANGUAGE)
    val THEME = stringPreferencesKey(PreferencesConstants.KEY_THEME)
}

class PreferencesManager(private val context: Context) {

    val languageFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.LANGUAGE] ?: PreferencesConstants.DEFAULT_LANGUAGE
    }

    val themeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.THEME] ?: PreferencesConstants.DEFAULT_THEME
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.LANGUAGE] = lang
        }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.THEME] = theme
        }
    }
}

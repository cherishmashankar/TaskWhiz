package com.example.taskwhiz.data.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val LANGUAGE = stringPreferencesKey(PreferencesConstants.KEY_LANGUAGE)
    val THEME = stringPreferencesKey(PreferencesConstants.KEY_THEME)
}
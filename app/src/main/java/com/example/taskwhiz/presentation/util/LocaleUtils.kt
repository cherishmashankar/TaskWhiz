package com.example.taskwhiz.presentation.util

import android.content.Context
import com.example.taskwhiz.domain.model.Language
import java.util.Locale

fun Context.updateLocale(language: Language): Context {
    val locale = when (language) {
        Language.ENGLISH -> Locale("en")
        Language.GERMAN -> Locale("de")
    }
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    return createConfigurationContext(config)
}
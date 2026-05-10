package com.example.taskwhiz.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
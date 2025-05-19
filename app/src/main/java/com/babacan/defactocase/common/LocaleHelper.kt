package com.babacan.defactocase.common

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object LocaleHelper {
    fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeList = LocaleList(locale)
            context.getSystemService(LocaleManager::class.java)
                ?.applicationLocales = android.os.LocaleList(locale)
            context
        } else {
            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration)
        }
    }

    fun getLangCode(context: Context): String {
        val locale = context.resources.configuration.locales[0]
        return locale.language
    }
}
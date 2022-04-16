package com.wahyuhw.githubuserapp.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object DataStoreUtil {
    const val THEME_NAME = "USER_THEME"
    val THEME_KEY = booleanPreferencesKey("THEME_PREFERENCES_KEY")
}
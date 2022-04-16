package com.wahyuhw.githubuserapp.data.prefs

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.wahyuhw.githubuserapp.utils.DataStoreUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences (private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DataStoreUtil.THEME_NAME)

    fun getThemeSetting(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[DataStoreUtil.THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DataStoreUtil.THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(context: Context): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
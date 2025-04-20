package com.jchunga.musicapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    private val URI_KEY = stringPreferencesKey("music_folder_uri")

    suspend fun saveUri(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[URI_KEY] = uri
        }
    }

    fun getUri(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[URI_KEY]
    }

    suspend fun clearUri() {
        context.dataStore.edit { preferences ->
            preferences.remove(URI_KEY)
        }
    }

}
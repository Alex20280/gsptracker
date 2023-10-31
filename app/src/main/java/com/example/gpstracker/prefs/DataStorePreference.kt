package com.example.gpstracker.prefs

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStorePreference @Inject constructor(
     private val myDataStore: DataStore<Preferences>
) {

    suspend fun saveData(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        myDataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun getData(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = myDataStore.data.first()
        return preferences[preferencesKey]
    }
}
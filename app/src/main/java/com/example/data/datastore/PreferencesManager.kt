package com.example.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "status_saver_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_WHATSAPP_URI = stringPreferencesKey("pref_whatsapp_tree_uri")
        val KEY_BUSINESS_URI = stringPreferencesKey("pref_business_tree_uri")
        val KEY_AUTO_SAVE_ENABLED = booleanPreferencesKey("pref_auto_save_enabled")
        val KEY_AUTO_SAVE_INTERVAL = intPreferencesKey("pref_auto_save_interval")
        val KEY_BUSINESS_ENABLED = booleanPreferencesKey("pref_business_enabled")
        val KEY_PROCESSED_FILES = stringSetPreferencesKey("pref_processed_file_signatures")
    }

    val whatsappTreeUri: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_WHATSAPP_URI]
    }

    val businessTreeUri: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_BUSINESS_URI]
    }

    val isAutoSaveEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUTO_SAVE_ENABLED] ?: false
    }

    val autoSaveIntervalHours: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUTO_SAVE_INTERVAL] ?: 1
    }

    val isBusinessEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_BUSINESS_ENABLED] ?: false
    }

    val processedFileSignatures: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_PROCESSED_FILES] ?: emptySet()
    }

    suspend fun setWhatsAppTreeUri(uriString: String?) {
        context.dataStore.edit { prefs ->
            if (uriString == null) {
                prefs.remove(KEY_WHATSAPP_URI)
            } else {
                prefs[KEY_WHATSAPP_URI] = uriString
            }
        }
    }

    suspend fun setBusinessTreeUri(uriString: String?) {
        context.dataStore.edit { prefs ->
            if (uriString == null) {
                prefs.remove(KEY_BUSINESS_URI)
            } else {
                prefs[KEY_BUSINESS_URI] = uriString
            }
        }
    }

    suspend fun setAutoSaveEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTO_SAVE_ENABLED] = enabled
        }
    }

    suspend fun setAutoSaveInterval(hours: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTO_SAVE_INTERVAL] = hours
        }
    }

    suspend fun setBusinessEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_BUSINESS_ENABLED] = enabled
        }
    }

    suspend fun markFileProcessed(signature: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_PROCESSED_FILES] ?: emptySet()
            prefs[KEY_PROCESSED_FILES] = current + signature
        }
    }

    suspend fun markFilesProcessed(signatures: Set<String>) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_PROCESSED_FILES] ?: emptySet()
            prefs[KEY_PROCESSED_FILES] = current + signatures
        }
    }
}

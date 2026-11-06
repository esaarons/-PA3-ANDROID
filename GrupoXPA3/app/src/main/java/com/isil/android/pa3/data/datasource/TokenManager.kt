package com.isil.android.pa3.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "sesion_prefs")

// Guarda, consulta y elimina el token de sesión usando DataStore
class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    val getToken: Flow<String?> = context.dataStore.data.map { prefs -> prefs[TOKEN_KEY] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs -> prefs.remove(TOKEN_KEY) }
    }
}

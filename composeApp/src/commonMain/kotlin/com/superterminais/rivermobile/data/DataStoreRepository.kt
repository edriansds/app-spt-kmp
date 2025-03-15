package com.superterminais.rivermobile.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.superterminais.rivermobile.screens.synchronization.SyncType
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

class DataStoreRepository(private val prefs: DataStore<Preferences>) {

    companion object {
        private val LAST_SYNC_DATE_KEY = stringPreferencesKey("last_sync_date")
        private val LAST_DISCOUNTS_SYNC_DATE_KEY = stringPreferencesKey("last_discounts_sync_date")
        private val LAST_EXTENSIONS_SYNC_DATE_KEY =
            stringPreferencesKey("last_extensions_sync_date")

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")

        private val currentTimeZone = TimeZone.currentSystemDefault()
        private val dateTimeFormat = LocalDateTime.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
            char(' ')
            hour()
            char(':')
            minute()
        }
    }

    suspend fun saveUserSession(token: String, userId: String, userName: String) {
        prefs.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID] = userId
            preferences[USER_NAME] = userName
        }
    }

    suspend fun getUserSession(): Triple<String?, String?, String?> {
        val preferences = prefs.data.first()
        return Triple(preferences[USER_ID], preferences[USER_NAME], preferences[TOKEN_KEY])
    }

    suspend fun getUserId(): String? {
        val preferences = prefs.data.first()
        return preferences[USER_ID]
    }

    suspend fun clearUserSession() {
        prefs.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(USER_ID)
            preferences.remove(USER_NAME)
        }
    }

    suspend fun saveToken(token: String) {
        prefs.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveLastSyncDate(type: SyncType) {
        val date = Clock.System.now().toLocalDateTime(currentTimeZone).format(dateTimeFormat)

        prefs.edit { preferences ->
            when (type) {
                SyncType.Discounts -> {
                    preferences[LAST_DISCOUNTS_SYNC_DATE_KEY] = date
                }

                SyncType.Extensions -> {
                    preferences[LAST_EXTENSIONS_SYNC_DATE_KEY] = date
                }
            }
        }
    }

    suspend fun getLastSyncDate(type: SyncType): String? {
        val preferences = prefs.data.first()
        return preferences.run {
            when (type) {
                SyncType.Discounts -> {
                    get(LAST_DISCOUNTS_SYNC_DATE_KEY)
                }

                SyncType.Extensions -> {
                    get(LAST_EXTENSIONS_SYNC_DATE_KEY)
                }
            }
        }
    }

    suspend fun clearSyncDates() {
        prefs.edit { preferences ->
            preferences.remove(LAST_DISCOUNTS_SYNC_DATE_KEY)
            preferences.remove(LAST_EXTENSIONS_SYNC_DATE_KEY)
        }
    }
}
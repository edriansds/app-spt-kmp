package com.superterminais.rivermobile.screens.synchronization

sealed class SyncType {
    object Discounts : SyncType()
    object Extensions : SyncType()
}

class SyncDataStore {
//    companion object {
//        private val Context.dataStore by preferencesDataStore(name = "sync_data")
//        private val LAST_SYNC_DATE_KEY = stringPreferencesKey("last_sync_date")
//        private val LAST_DISCOUNTS_SYNC_DATE_KEY = stringPreferencesKey("last_discounts_sync_date")
//        private val LAST_EXTENSIONS_SYNC_DATE_KEY = stringPreferencesKey("last_extensions_sync_date")
//
//        private const val DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"
//    }
//
//    suspend fun saveLastSyncDate(context: Context, type: SyncType) {
//        val lastSyncDate = DateFormat.format(DATE_FORMAT, System.currentTimeMillis())
//            .toString()
//
//        context.dataStore.edit { preferences ->
//            when (type) {
//                SyncType.Discounts -> {
//                    preferences[LAST_DISCOUNTS_SYNC_DATE_KEY] = lastSyncDate
//                }
//
//                SyncType.Extensions -> {
//                    preferences[LAST_EXTENSIONS_SYNC_DATE_KEY] = lastSyncDate
//                }
//            }
//        }
//    }
//
//    suspend fun getLastSyncDate(context: Context, type: SyncType): String? {
//        val preferences = context.dataStore.data.first()
//        return preferences.run {
//            when (type) {
//                SyncType.Discounts -> {
//                    get(LAST_DISCOUNTS_SYNC_DATE_KEY)
//                }
//
//                SyncType.Extensions -> {
//                    get(LAST_EXTENSIONS_SYNC_DATE_KEY)
//                }
//            }
//        }
//    }
//
//    suspend fun clearSyncDates(context: Context) {
//        context.dataStore.edit { preferences ->
//            preferences.remove(LAST_DISCOUNTS_SYNC_DATE_KEY)
//            preferences.remove(LAST_EXTENSIONS_SYNC_DATE_KEY)
//        }
//    }
}
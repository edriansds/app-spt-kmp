package com.superterminais.rivermobile

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.superterminais.rivermobile.data.DATA_STORE_FILE_NAME

fun createDataStore(context: Context): DataStore<Preferences> {
    return com.superterminais.rivermobile.data.createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}
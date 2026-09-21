package kr.playax.novel.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "playax_novel_settings")

class UserSettingsRepository(private val context: Context) {
    private val adultKey = booleanPreferencesKey("adult_unlocked")
    private val hideAdultKey = booleanPreferencesKey("hide_adult_passages")

    val adultUnlocked: Flow<Boolean> = context.settingsDataStore.data.map { it[adultKey] == true }
    val hideAdultPassages: Flow<Boolean> = context.settingsDataStore.data.map { it[hideAdultKey] == true }

    suspend fun setAdultUnlocked(value: Boolean) {
        context.settingsDataStore.edit { it[adultKey] = value }
    }

    suspend fun setHideAdultPassages(value: Boolean) {
        context.settingsDataStore.edit { it[hideAdultKey] = value }
    }
}

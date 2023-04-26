package dev.thorcode.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.thorcode.storyapp.api.ApiConfig
import dev.thorcode.storyapp.data.UserPreference
import dev.thorcode.storyapp.data.UserRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

object DeInjection {
    fun repo(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository(pref, apiService)
    }
}
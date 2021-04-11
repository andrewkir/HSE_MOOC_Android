package ru.andrewkir.hse_mooc.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ru.andrewkir.hse_mooc.R


class UserPrefsManager(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val prefs: SharedPreferences

    init {
        prefs = applicationContext.getSharedPreferences(
            applicationContext.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }

    fun saveAccessToken(token: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    fun obtainAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }


    fun saveRefreshToken(token: String) {
        val editor = prefs.edit()
        editor.putString(REFRESH_TOKEN, token)
        editor.apply()
    }

    fun obtainRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}

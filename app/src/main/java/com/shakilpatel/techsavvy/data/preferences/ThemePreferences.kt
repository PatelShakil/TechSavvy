package com.shakilpatel.techsavvy.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.shakilpatel.techsavvy.ui.theme.AppTheme

class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "app_theme"
        private const val KEY_DARK_MODE = "dark_mode"
    }

    fun getTheme(): AppTheme {
        val themeName = prefs.getString(KEY_THEME, AppTheme.TECH_SAVVY_BLUE.name)
        return try {
            AppTheme.valueOf(themeName ?: AppTheme.TECH_SAVVY_BLUE.name)
        } catch (e: Exception) {
            AppTheme.TECH_SAVVY_BLUE
        }
    }

    fun setTheme(theme: AppTheme) {
        prefs.edit().putString(KEY_THEME, theme.name).apply()
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }
}

package com.upf.memorytrace_android.util

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate


object ThemeUtil {
    enum class ThemeMode {
        DEFAULT, LIGHT, DARK;

        companion object {
            fun of(value: Int): ThemeMode = values().find { it.ordinal == value } ?: DEFAULT
        }
    }

    fun applyTheme() {
        val mode = MemoryTraceConfig.theme
        applyTheme(ThemeMode.of(mode))
    }

    fun applyTheme(mode: ThemeMode) {
        when (mode) {
            ThemeMode.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            ThemeMode.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // 안드로이드 10 이상
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    // 안드로이드 10 미만
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
            }
        }
    }
}

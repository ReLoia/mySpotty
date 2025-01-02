package it.reloia.myspotty.model

import me.zhanghai.compose.preference.Preferences

object AppPreferences {
}

inline fun <reified T> Preferences.getOrDefault(key: String, defaultValue: T): T {
    return this.asMap().getOrDefault(key, defaultValue) as? T ?: defaultValue
}
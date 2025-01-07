package it.reloia.myspotty.core.preferences

import android.content.SharedPreferences
import me.zhanghai.compose.preference.Preferences

fun SharedPreferences.getApiURL(): String {
    return (this.getString("api_url", "") ?: "").let {
        if (it.isEmpty()) return@let it
        if (!it.endsWith("/")) "$it/" else it
    }
}


inline fun <reified T> Preferences.getOrDefault(key: String, defaultValue: T): T {
    return this.asMap().getOrDefault(key, defaultValue) as? T ?: defaultValue
}
package com.pr0gramm3r101.utils.settings

import java.util.prefs.Preferences

class DesktopSettings : Settings {
    private val preferences = Preferences.userRoot().node("ru.twoxconnect")

    override fun putString(key: String, value: String) {
        preferences.put(key, value)
        preferences.flush()
    }

    override fun getString(key: String, default: String): String {
        return preferences.get(key, default)
    }

    override fun putInt(key: String, value: Int) {
        preferences.putInt(key, value)
        preferences.flush()
    }

    override fun getInt(key: String, default: Int): Int {
        return preferences.getInt(key, default)
    }

    override fun putLong(key: String, value: Long) {
        preferences.putLong(key, value)
        preferences.flush()
    }

    override fun getLong(key: String, default: Long): Long {
        return preferences.getLong(key, default)
    }

    override fun putFloat(key: String, value: Float) {
        preferences.putFloat(key, value)
        preferences.flush()
    }

    override fun getFloat(key: String, default: Float): Float {
        return preferences.getFloat(key, default)
    }

    override fun putBoolean(key: String, value: Boolean) {
        preferences.putBoolean(key, value)
        preferences.flush()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return preferences.getBoolean(key, default)
    }

    override fun putStringSet(key: String, value: Set<String>) {
        preferences.put(key, value.joinToString(separator = "\u0000"))
        preferences.flush()
    }

    override fun getStringSet(key: String, default: Set<String>): Set<String> {
        val value = preferences.get(key, null) ?: return default
        return value.split("\u0000").toSet()
    }

    override fun putStringList(key: String, value: List<String>) {
        preferences.put(key, value.joinToString(separator = "\u0000"))
        preferences.flush()
    }

    override fun getStringList(key: String, default: List<String>): List<String> {
        val value = preferences.get(key, null) ?: return default
        return value.split("\u0000")
    }

    override fun remove(key: String) {
        preferences.remove(key)
        preferences.flush()
    }
} 
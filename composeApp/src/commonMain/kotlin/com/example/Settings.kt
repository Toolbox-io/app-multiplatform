package com.example

import com.pr0gramm3r101.utils.settings.Settings
import com.example.ui.Theme

object Settings {
    val settings = Settings()

    var materialYou
        get() = settings.getBoolean("materialYou", true)
        set(value) = settings.putBoolean("materialYou", value)

    var theme: Theme
        get() = Theme.entries[settings.getInt("theme", Theme.AsSystem.ordinal)]
        set(value) = settings.putInt("theme", value.ordinal)
}
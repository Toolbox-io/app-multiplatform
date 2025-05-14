@file:Suppress("unused")
package io.toolbox

import com.pr0gramm3r101.utils.decrypt
import com.pr0gramm3r101.utils.encrypt
import com.pr0gramm3r101.utils.settings.Settings
import io.toolbox.ui.Theme
import kotlin.random.Random

object Settings {
    val settings = Settings()

    var materialYou
        get() = settings.getBoolean("materialYou", true)
        set(value) = settings.putBoolean("materialYou", value)

    var theme: Theme
        get() = Theme.entries[settings.getInt("theme", Theme.AsSystem.ordinal)]
        set(value) = settings.putInt("theme", value.ordinal)

    const val ALLOW_BIOMETRIC_LABEL = "erjgeskh"
    const val APPLOCKER_RANDOM_KEY_LABEL = "ejn"
    const val APPLOCKER_ENCRYPTED_PASSWORD_LABEL = "hedrh"
    const val APP_RANDOM_KEY_LABEL = "soeitge"
    const val APP_ENCRYPTED_PASSWORD_LABEL = "waegnwg"

    const val UPDATE_DSA_LABEL = "gsmwsojgnwg"
    const val DONT_SHOW_IN_RECENTS_LABEL = "grehbes"
    const val MATERIAL_YOU_ENABLED_LABEL = "vghwsjkrgn"
    const val APP_THEME_LABEL = "hgebngahnbe"
    const val ALARM_LABEL = "hbhnli"
    const val CURRENT_CUSTOM_ALARM_LABEL = "nghworhn"
    const val CUSTOM_ALARMS_LABEL = "qwaeftgn"
    const val INTRUDER_PHOTO_LABEL = "gwrsgbn"
    const val INTRUDER_PHOTO_NOPT_LABEL = "qglnqnegf"
    const val SELECTED_APPS_LABEL = "gnwlisohnrsb"
    const val UNLOCK_MODE_LABEL = "bnsrllhw"
    const val ENABLED_LABEL = "whgbnwrohn"
    const val UNLOCK_ATTEMPTS_LABEL = "gewrnwh"
    const val FG_SERVICE_ENABLED_LABEL = "hbjnwsokehgr"
    const val SLEEP_LABEL = "hbgewsrjkhn"
    const val USED_LABEL = "jtesnhjsertjsr"
    const val REPLACE_PHOTOS_WITH_INTRUDER_LABEL = "replacePhotosWithIntruder"
    const val REMOVE_DUPLICATES_LABEL = "removeDuplicates"
    const val REMOVE_USELESS_NOTIFICATIONS_LABEL = "removeUselessNotifications"

    var update_dsa
        get() = settings.getBoolean(UPDATE_DSA_LABEL, false)
        set(value) {
            settings.putBoolean(UPDATE_DSA_LABEL, value)
        }

    var allowBiometric
        get() = settings.getBoolean(ALLOW_BIOMETRIC_LABEL, false)
        set(value) {
            settings.putBoolean(ALLOW_BIOMETRIC_LABEL, value)
        }

    var dontShowInRecents
        get() = settings.getBoolean(DONT_SHOW_IN_RECENTS_LABEL, false)
        set(value) {
            settings.putBoolean(DONT_SHOW_IN_RECENTS_LABEL, value)
        }

    var materialYouEnabled
        get() = settings.getBoolean(MATERIAL_YOU_ENABLED_LABEL, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        set(value) {
            settings.putBoolean(MATERIAL_YOU_ENABLED_LABEL, value)
        }

    var appTheme: Theme
        get() = Theme.entries[settings.getInt(APP_THEME_LABEL, Theme.AsSystem.ordinal)]
        set(value) {
            settings.putInt(APP_THEME_LABEL, value.ordinal)
        }

    object Actions {
        object Alarm {
            var enabled
                get() = settings.getBoolean(ALARM_LABEL, false)
                set(value) {
                    settings.putBoolean(ALARM_LABEL, value)
                }

            var current
                get() = settings.getString(CURRENT_CUSTOM_ALARM_LABEL, "")
                set(value) {
                    settings.putString(CURRENT_CUSTOM_ALARM_LABEL, value)
                }

            var customAlarms: Set<String>
                get() = settings.getStringSet(CUSTOM_ALARMS_LABEL, setOf())
                set(value) {
                    settings.putStringSet(CUSTOM_ALARMS_LABEL, value)
                }
        }
        object IntruderPhoto {
            var enabled
                get() = settings.getBoolean(INTRUDER_PHOTO_LABEL, false)
                set(value) {
                    settings.putBoolean(INTRUDER_PHOTO_LABEL, value)
                }

            var nopt
                get() = settings.getBoolean(INTRUDER_PHOTO_NOPT_LABEL, false)
                set(value) {
                    settings.putBoolean(INTRUDER_PHOTO_NOPT_LABEL, value)
                }
        }

        inline fun run() = runActions()
    }

    object Keys {
        interface Key {
            fun set(password: String)
            fun check(password: String): Boolean
            val isSet: Boolean
        }

        private fun generateKey() =
            Random.nextBytes(
                Random.nextInt(1, 16)
            )
                .map {
                    it.toInt().toChar()
                }
                .joinToString("")

        object Applocker: Key {
            private var randomKey: String
                get() {
                    var result = settings.getString(APPLOCKER_RANDOM_KEY_LABEL, "")
                    if (result.isBlank()) {
                        result = generateKey()
                        randomKey = result
                    }
                    return result
                }
                set(value) {
                    settings.putString(APPLOCKER_RANDOM_KEY_LABEL, value)
                }

            private var encryptedPassword
                get() = settings.getString(APPLOCKER_ENCRYPTED_PASSWORD_LABEL, "")
                set(value) {
                    settings.putString(APPLOCKER_ENCRYPTED_PASSWORD_LABEL, value)
                }

            override fun set(password: String) {
                randomKey = generateKey()
                encryptedPassword =
                    if (password.isEmpty()) ""
                    else randomKey.encrypt(password)
            }

            override fun check(password: String): Boolean {
                if (encryptedPassword.isEmpty() && password.isEmpty()) {
                    return true
                }
                val decryptedPassword = try {
                    encryptedPassword.decrypt(password)
                } catch (_: Exception) {
                    null
                }
                return decryptedPassword != null && decryptedPassword == randomKey
            }

            override val isSet get() = encryptedPassword.isNotEmpty()
        }

        object App: Key {
            private var randomKey: String
                get() {
                    var result = settings.getString(APP_RANDOM_KEY_LABEL, "")
                    if (result.isBlank()) {
                        result = generateKey()
                        randomKey = result
                    }
                    return result
                }
                set(value) {
                    settings.putString(APP_RANDOM_KEY_LABEL, value)
                }

            private var encryptedPassword
                get() = settings.getString(APP_ENCRYPTED_PASSWORD_LABEL, "")
                set(value) {
                    settings.putString(APP_ENCRYPTED_PASSWORD_LABEL, value)
                }

            override fun set(password: String) {
                if (password.isNotEmpty()) randomKey = generateKey()
                encryptedPassword =
                    if (password.isEmpty()) ""
                    else randomKey.encrypt(password)
            }

            override fun check(password: String): Boolean {
                if (encryptedPassword.isEmpty() && password.isEmpty()) {
                    return true
                }
                val decryptedPassword = try {
                    encryptedPassword.decrypt(password)
                } catch (_: Exception) {
                    null
                }
                return decryptedPassword != null && decryptedPassword == randomKey
            }

            override val isSet get() = encryptedPassword.isNotEmpty()
        }
    }

    object Applocker {
        var enabled
            get() = settings.getBoolean(ENABLED_LABEL, false)
            set(value) {
                settings.putBoolean(ENABLED_LABEL, value)
            }

        object UnlockMode {
            const val NOTHING_SELECTED = -1
            const val LONG_PRESS_APP_INFO = 0
            const val LONG_PRESS_CLOSE = 1
            const val LONG_PRESS_TITLE = 2
            const val PRESS_TITLE = 3
            const val LONG_PRESS_OPEN_APP_AGAIN = 4
        }

        var apps: Set<String>
            get() = settings.getStringSet(SELECTED_APPS_LABEL, setOf())
            set(value) {
                settings.putStringSet(SELECTED_APPS_LABEL, value)
            }

        // TODO fix
        private val DEFAULT_UNLOCK_MODE =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                UnlockMode.LONG_PRESS_APP_INFO
            else
                UnlockMode.LONG_PRESS_OPEN_APP_AGAIN

        var unlockMode: Int
            get() = settings.getInt(UNLOCK_MODE_LABEL, DEFAULT_UNLOCK_MODE).let {
                return if (it == UnlockMode.NOTHING_SELECTED) DEFAULT_UNLOCK_MODE else it
            }
            set(value) {
                if (value in 0..UnlockMode.LONG_PRESS_OPEN_APP_AGAIN) {
                    settings.putInt(UNLOCK_MODE_LABEL, value)
                } else {
                    throw IllegalArgumentException("The argument must be from 0 to PRESS_TITLE.")
                }
            }

        // TODO fix
        fun getUnlockModeDescription(value: Int, resources: Resources) = when (value) {
            UnlockMode.LONG_PRESS_APP_INFO -> resources.getString(R.string.lp_ai)
            UnlockMode.LONG_PRESS_CLOSE -> resources.getString(R.string.lp_c)
            UnlockMode.LONG_PRESS_TITLE -> resources.getString(R.string.lp_t)
            UnlockMode.PRESS_TITLE -> resources.getString(R.string.p_t)
            UnlockMode.LONG_PRESS_OPEN_APP_AGAIN -> resources.getString(R.string.lp_oaa)
            else -> ""
        }

        var used: Boolean
            get() = settings.getBoolean(USED_LABEL, false)
            set(value) {
                settings.putBoolean(USED_LABEL, value)
            }
    }

    object UnlockProtection {
        var enabled
            get() = settings.getBoolean(ENABLED_LABEL, false)
            set(value) {
                settings.putBoolean(ENABLED_LABEL, value)
            }

        var unlockAttempts
            get() = settings.getInt(UNLOCK_ATTEMPTS_LABEL, 2)
            set(value) {
                settings.putInt(UNLOCK_ATTEMPTS_LABEL, value)
            }

        var fgServiceEnabled
            get() = settings.getBoolean(FG_SERVICE_ENABLED_LABEL, true)
            set(value) {
                settings.putBoolean(FG_SERVICE_ENABLED_LABEL, value)
            }

        val Alarm = Actions.Alarm
        val IntruderPhoto = Actions.IntruderPhoto
    }

    object Tiles {
        var sleep
            get() = settings.getBoolean(SLEEP_LABEL, false)
            set(value) {
                settings.putBoolean(SLEEP_LABEL, value)
            }
    }

    object Notifications {
        operator fun get(type: String): Boolean {
            return settings.getBoolean(type, true)
        }

        operator fun set(type: String, value: Boolean) {
            settings.putBoolean(type, value)
        }
    }

    object NotificationHistory {
        var enabled
            get() = settings.getBoolean(ENABLED_LABEL, false)
            set(value) {
                settings.putBoolean(ENABLED_LABEL, value)
            }

        var removeDuplicates
            get() = settings.getBoolean(REMOVE_DUPLICATES_LABEL, true)
            set(value) {
                settings.putBoolean(REMOVE_DUPLICATES_LABEL, value)
            }

        var removeUselessNotifications
            get() = settings.getBoolean(REMOVE_USELESS_NOTIFICATIONS_LABEL, true)
            set(value) {
                settings.putBoolean(REMOVE_USELESS_NOTIFICATIONS_LABEL, value)
            }

        var apps: Set<String>
            get() = settings.getStringSet(
                SELECTED_APPS_LABEL,
                appsDefault()
            )
            set(value) {
                settings.putStringSet(SELECTED_APPS_LABEL, value)
            }
    }

    object Developer {
        var replacePhotosWithIntruder
            get() = settings.getBoolean(REPLACE_PHOTOS_WITH_INTRUDER_LABEL, false)
            set(value) {
                settings.putBoolean(REPLACE_PHOTOS_WITH_INTRUDER_LABEL, value)
            }
    }
}

expect fun appsDefault(): Set<String>

expect fun runActions()
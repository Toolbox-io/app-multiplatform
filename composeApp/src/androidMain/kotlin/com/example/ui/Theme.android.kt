package com.example.ui

import android.app.Activity
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.pr0gramm3r101.utils.invoke
import com.example.TwoXConnectApp

actual fun colorScheme(darkTheme: Boolean): ColorScheme {
    return if (Build.VERSION.SDK_INT >= 31 && dynamicThemeEnabled) {
        if (darkTheme) {
            dynamicDarkColorScheme(TwoXConnectApp.context)
        } else {
            dynamicLightColorScheme(TwoXConnectApp.context)
        }
    } else {
        if (darkTheme) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }
    }
}

@Composable
actual inline fun ProvideContextMenuRepresentation(darkTheme: Boolean, content: @Composable () -> Unit) {
    content()
}

@Composable
actual fun fixStatusBar(darkTheme: Boolean, asSystem: Boolean) {
    WindowCompat.getInsetsController((LocalContext() as Activity).window, LocalView()).isAppearanceLightStatusBars = !darkTheme
}
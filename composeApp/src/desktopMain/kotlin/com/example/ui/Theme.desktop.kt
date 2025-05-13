package com.example.ui

import androidx.compose.foundation.DarkDefaultContextMenuRepresentation
import androidx.compose.foundation.LightDefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

actual fun colorScheme(darkTheme: Boolean) = if (darkTheme) darkColorScheme() else lightColorScheme()

@Composable
actual inline fun ProvideContextMenuRepresentation(darkTheme: Boolean, crossinline content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContextMenuRepresentation provides if (darkTheme) {
            DarkDefaultContextMenuRepresentation
        } else {
            LightDefaultContextMenuRepresentation
        }
    ) {
        content()
    }
}

@Composable
actual fun fixStatusBar(darkTheme: Boolean, asSystem: Boolean) {}
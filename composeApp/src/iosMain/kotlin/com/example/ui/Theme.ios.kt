package com.example.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

actual fun colorScheme(darkTheme: Boolean) = if (darkTheme) darkColorScheme() else lightColorScheme()

@Composable
actual inline fun ProvideContextMenuRepresentation(darkTheme: Boolean, content: @Composable () -> Unit) {
    content()
}

@Suppress("UNCHECKED_CAST")
@Composable
actual fun fixStatusBar(darkTheme: Boolean, asSystem: Boolean) {
    UIApplication
        .sharedApplication
        .connectedScenes
        .flatMap { (it as? UIWindowScene)?.windows as? List<UIWindow> ?: listOf() }
        .first { it.isKeyWindow() }
        .overrideUserInterfaceStyle = when {
            asSystem -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            darkTheme -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
            !darkTheme -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
            else -> error("")
        }
}
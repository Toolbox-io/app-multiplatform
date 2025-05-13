package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import com.pr0gramm3r101.utils.LocalSupportClipboardManager
import com.pr0gramm3r101.utils.ToggleNavScrimEffect
import com.pr0gramm3r101.utils.Typography
import com.pr0gramm3r101.utils.invoke
import com.pr0gramm3r101.utils.plus
import com.pr0gramm3r101.utils.supportClipboardManagerImpl
import com.example.Res
import com.example.Settings
import com.example.roboto

interface WindowInsetsScope {
    val safeDrawingInsets: WindowInsets

    val isTopInsetConsumed: Boolean
    val isBottomInsetConsumed: Boolean
    val isLeftInsetConsumed: Boolean
    val isRightInsetConsumed: Boolean

    val topInset: Int
    val bottomInset: Int
    val leftInset: Int
    val rightInset: Int
}

@Composable
expect inline fun ProvideContextMenuRepresentation(darkTheme: Boolean, crossinline content: @Composable () -> Unit)

expect fun colorScheme(darkTheme: Boolean): ColorScheme

val LocalWindowInsetsScope: ProvidableCompositionLocal<WindowInsetsScope> = compositionLocalOf { error("") }

enum class Theme {
    AsSystem,
    Light,
    Dark
}

var dynamicThemeEnabled by mutableStateOf(
    runCatching { Settings.materialYou }.getOrNull() == true
)
var theme by mutableStateOf(
    runCatching { Settings.theme }.getOrNull() ?: Theme.AsSystem
)

@Composable
expect fun fixStatusBar(darkTheme: Boolean, asSystem: Boolean)

@Composable
fun AppTheme(
    darkTheme: Boolean = when (theme) {
        Theme.AsSystem -> isSystemInDarkTheme()
        Theme.Light -> false
        Theme.Dark -> true
    },
    removeScrim: Boolean = true,
    consumeTopInsets: Boolean = false,
    consumeBottomInsets: Boolean = false,
    consumeLeftInsets: Boolean = false,
    consumeRightInsets: Boolean = false,
    content: @Composable WindowInsetsScope.() -> Unit
) {
    var colorScheme = colorScheme(darkTheme)

    val primary by animateColorAsState(colorScheme.primary)
    val onPrimary by animateColorAsState(colorScheme.onPrimary)
    val primaryContainer by animateColorAsState(colorScheme.primaryContainer)
    val onPrimaryContainer by animateColorAsState(colorScheme.onPrimaryContainer)
    val secondary by animateColorAsState(colorScheme.secondary)
    val onSecondary by animateColorAsState(colorScheme.onSecondary)
    val secondaryContainer by animateColorAsState(colorScheme.secondaryContainer)
    val onSecondaryContainer by animateColorAsState(colorScheme.onSecondaryContainer)
    val tertiary by animateColorAsState(colorScheme.tertiary)
    val onTertiary by animateColorAsState(colorScheme.onTertiary)
    val tertiaryContainer by animateColorAsState(colorScheme.tertiaryContainer)
    val onTertiaryContainer by animateColorAsState(colorScheme.onTertiaryContainer)
    val error by animateColorAsState(colorScheme.error)
    val onError by animateColorAsState(colorScheme.onError)
    val errorContainer by animateColorAsState(colorScheme.errorContainer)
    val onErrorContainer by animateColorAsState(colorScheme.onErrorContainer)
    val background by animateColorAsState(colorScheme.background)
    val onBackground by animateColorAsState(colorScheme.onBackground)
    val surface by animateColorAsState(colorScheme.surface)
    val onSurface by animateColorAsState(colorScheme.onSurface)
    val surfaceVariant by animateColorAsState(colorScheme.surfaceVariant)
    val onSurfaceVariant by animateColorAsState(colorScheme.onSurfaceVariant)
    val outline by animateColorAsState(colorScheme.outline)
    val outlineVariant by animateColorAsState(colorScheme.outlineVariant)
    val scrim by animateColorAsState(colorScheme.scrim)
    val inverseSurface by animateColorAsState(colorScheme.inverseSurface)
    val inverseOnSurface by animateColorAsState(colorScheme.inverseOnSurface)
    val inversePrimary by animateColorAsState(colorScheme.inversePrimary)
    val surfaceDim by animateColorAsState(colorScheme.surfaceDim)
    val surfaceBright by animateColorAsState(colorScheme.surfaceBright)
    val surfaceContainerLowest by animateColorAsState(colorScheme.surfaceContainerLowest)
    val surfaceContainerLow by animateColorAsState(colorScheme.surfaceContainerLow)
    val surfaceContainer by animateColorAsState(colorScheme.surfaceContainer)
    val surfaceContainerHigh by animateColorAsState(colorScheme.surfaceContainerHigh)
    val surfaceContainerHighest by animateColorAsState(colorScheme.surfaceContainerHighest)

    colorScheme = colorScheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        inversePrimary = inversePrimary,
        surfaceDim = surfaceDim,
        surfaceBright = surfaceBright,
        surfaceContainerLowest = surfaceContainerLowest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest
    )

    fixStatusBar(darkTheme, theme == Theme.AsSystem)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(Res.font.roboto)
    ) {
        ToggleNavScrimEffect(!removeScrim)

        val insets = WindowInsets(
            WindowInsets.systemBars.getLeft(
                LocalDensity(),
                LocalLayoutDirection()
            ),
            WindowInsets.systemBars.getTop(LocalDensity()),
            WindowInsets.systemBars.getRight(
                LocalDensity(),
                LocalLayoutDirection()
            ),
            WindowInsets.systemBars.getBottom(LocalDensity())
        )

        ProvideContextMenuRepresentation(darkTheme) {
            CompositionLocalProvider(
                LocalSupportClipboardManager provides supportClipboardManagerImpl
            ) {
                WindowInsetsHandler(
                    handleLeft = !consumeLeftInsets,
                    handleRight = !consumeRightInsets
                ) {
                    Surface(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.let {
                            var mod = it as Modifier
                            if (consumeTopInsets) {
                                mod += Modifier.consumeWindowInsets(WindowInsets.safeContent.only(WindowInsetsSides.Top))
                            }
                            if (consumeBottomInsets) {
                                mod += Modifier.consumeWindowInsets(WindowInsets.safeContent.only(WindowInsetsSides.Bottom))
                            }
                            if (!consumeLeftInsets) {
                                mod += Modifier.consumeWindowInsets(WindowInsets.safeContent.only(WindowInsetsSides.Left))
                            }
                            if (!consumeRightInsets) {
                                mod += Modifier.consumeWindowInsets(WindowInsets.safeContent.only(WindowInsetsSides.Right))
                            }
                            mod
                        }
                    ) {
                        val topInset = insets.getTop(LocalDensity())
                        val bottomInset = insets.getBottom(LocalDensity())
                        val leftInset = insets.getLeft(LocalDensity(), LocalLayoutDirection())
                        val rightInset = insets.getRight(LocalDensity(), LocalLayoutDirection())

                        val scope = object : WindowInsetsScope {
                            override val safeDrawingInsets get() = insets
                            override val isTopInsetConsumed get() = consumeTopInsets
                            override val isBottomInsetConsumed get() = consumeBottomInsets
                            override val isLeftInsetConsumed get() = consumeLeftInsets
                            override val isRightInsetConsumed get() = consumeRightInsets
                            override val topInset get() = topInset
                            override val bottomInset get() = bottomInset
                            override val leftInset get() = leftInset
                            override val rightInset get() = rightInset
                        }

                        CompositionLocalProvider(LocalWindowInsetsScope provides scope) {
                            content(scope)
                        }
                    }
                }
            }
        }
    }
}

@Composable
inline fun WindowInsetsHandler(
    modifier: Modifier = Modifier,
    handleLeft: Boolean = true,
    handleRight: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier.let {
            var mod: Modifier = it
            if (handleLeft || handleRight) {
                mod += Modifier.windowInsetsPadding(
                    WindowInsets.displayCutout.only(
                        when {
                            handleLeft && !handleRight -> WindowInsetsSides.Left
                            !handleLeft -> WindowInsetsSides.Right
                            else -> WindowInsetsSides.Left + WindowInsetsSides.Right
                        }
                    ),
                )
            }
            mod += modifier
            mod
        },
        content = content
    )
}
package com.pr0gramm3r101.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

actual fun Modifier.clearFocusOnKeyboardDismiss() = this

@Composable
actual fun ToggleNavScrimEffect(enabled: Boolean) {}
actual val materialYouAvailable get() = false
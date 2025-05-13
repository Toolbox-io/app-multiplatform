package io.toolbox

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.toolbox.ui.App
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TwoXConnect",
        icon = painterResource(Res.drawable.icon),
        state = rememberWindowState(width = 600.dp, height = 750.dp)
    ) {
        App()
    }
}
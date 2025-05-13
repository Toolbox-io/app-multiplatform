package com.example.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalNavController: ProvidableCompositionLocal<NavController> = compositionLocalOf { error("") }

@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()

        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = "main",
                enterTransition = { scaleIn(initialScale = 0.9f) + fadeIn() },
                exitTransition = { scaleOut(targetScale = 0.9f) + fadeOut() }
            ) {
                composable("main") { MainScreen() }
                composable("about") { AboutScreen() }
            }
        }
    }
}
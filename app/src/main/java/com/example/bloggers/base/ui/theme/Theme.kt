package com.example.bloggers.base.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


object AppTheme {
    val colors: Colors
        @Composable
        get() = if (isSystemInDarkTheme()) {
            DarkColors
        } else {
            LightColors
        }

    val gradient: List<Color>
        @Composable
        get() = if (isSystemInDarkTheme()) {
            DarkGradient
        } else {
            LightGradient
        }
}


@Composable
fun BloggersTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = AppTheme.colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
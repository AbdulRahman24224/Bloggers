package com.example.bloggers.base.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable


object AppTheme {
    val colors: Colors
        @Composable
        get() = if (isSystemInDarkTheme()) {
            DarkColors
        } else {
            LightColors
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
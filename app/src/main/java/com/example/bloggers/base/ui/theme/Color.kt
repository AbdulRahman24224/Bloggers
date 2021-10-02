package com.example.bloggers.base.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val graySurface = Color(0xFF2A2A2A)
fun surfaceGradient(isDark: Boolean) =
    if (isDark) listOf(graySurface, Color.Black) else listOf(Color.White, Color.LightGray)

private val Slate200 = Color(0xFF81A9B3)
private val Slate600 = Color(0xFF4A6572)
private val Slate800 = Color(0xFF232F34)

private val Orange500 = Color(0xFFF9AA33)
private val Orange700 = Color(0xFFC78522)


val Neutral8 = Color(0xff121212)
val Neutral7 = Color(0xde000000)
val Neutral6 = Color(0x99000000)
val Neutral5 = Color(0x61000000)
val Neutral4 = Color(0x1f000000)
val Neutral3 = Color(0x1fffffff)
val Neutral2 = Color(0x61ffffff)
val Neutral1 = Color(0xbdffffff)
val Neutral0 = Color(0xffffffff)

val DarkGradient = listOf<Color>( Orange500 , Neutral5)
val LightGradient = listOf<Color>( Orange500 , Neutral0)

val LightColors = lightColors(
    primary = Slate800,
    onPrimary = Color.White,
    primaryVariant = Slate600,
    secondary = Orange700,
    secondaryVariant = Orange500,
    onSecondary = Color.Black,
    background = Color.White
)

val DarkColors = darkColors(
    primary = Slate200,
    onPrimary = Color.Black,
    secondary = Orange500,
    onSecondary = Color.Black,
    background = Color.Black ,
    onBackground = graySurface
).withBrandedSurface()

fun Colors.withBrandedSurface() = copy(
    surface = primary.copy(alpha = 0.08f).compositeOver(this.surface),
)

const val AlphaNearOpaque = 0.95f
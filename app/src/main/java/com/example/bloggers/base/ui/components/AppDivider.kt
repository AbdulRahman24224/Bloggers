package com.example.bloggers.base.ui.components

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bloggers.base.ui.theme.AppTheme

private const val DividerAlpha = 0.12f

@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.onSecondary.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}
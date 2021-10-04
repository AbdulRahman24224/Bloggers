package com.example.bloggers.base.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bloggers.R
import com.example.bloggers.base.ui.theme.AppTheme
import com.example.bloggers.base.ui.theme.Neutral8
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun BackButton(modifier: Modifier = Modifier, upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = AppTheme.colors.primary,
            contentDescription = stringResource(R.string.label_back)
        )
    }
}

@Composable
fun RefreshButton(modifier: Modifier = Modifier, upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Refresh,
            tint = AppTheme.colors.primary,
            contentDescription = stringResource(R.string.label_back)
        )
    }

}

package com.example.bloggers.presentation.authors.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bloggers.R
import com.example.bloggers.base.ui.theme.AlphaNearOpaque
import com.example.bloggers.base.ui.theme.AppTheme
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun DestinationBar(modifier: Modifier = Modifier, onRefreshClicked: () -> Unit) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = AppTheme.colors.background.copy(alpha = AlphaNearOpaque),
            contentColor = AppTheme.colors.secondary,
            elevation = 0.dp,
        ) {
            Text(
                text = stringResource(id = R.string.our_authors),
                style = MaterialTheme.typography.h6,
                color = AppTheme.colors.secondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .align(CenterVertically)
                    .weight(1f),

                )
            IconButton(
                onClick = { onRefreshClicked.invoke() },
                modifier = Modifier.align(CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    tint = AppTheme.colors.secondary,
                    contentDescription = stringResource(R.string.our_authors)
                )
            }
        }

    }
}

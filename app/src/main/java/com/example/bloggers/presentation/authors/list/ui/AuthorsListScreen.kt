/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bloggers.presentation.authors.list.ui

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloggers.base.ui.components.AppCard
import com.example.bloggers.base.ui.components.AppSurface
import com.example.bloggers.base.ui.components.RoundImage
import com.example.bloggers.base.ui.theme.AppTheme
import com.example.bloggers.base.utils.SnackbarManager
import com.example.bloggers.base.utils.network.ConnectivityUtil.isConnectionOn
import com.example.bloggers.base.utils.resources.StringResourcesUtil.getStringValueOrNull
import com.example.bloggers.base.utils.ui.isScrolledToEnd
import com.example.bloggers.base.utils.ui.sV
import com.example.bloggers.presentation.authors.list.AuthorsListIntents
import com.example.bloggers.presentation.authors.list.AuthorsListViewModel
import com.example.data.entities.Author
import com.example.data.remote.Authors
import com.example.domain.usecases.authors.states.AuthorsListState


val AppBarHeight = 56.dp

@ExperimentalFoundationApi
@Composable
fun AuthorsListScreen(
    viewModel: AuthorsListViewModel = hiltViewModel(),
    onAuthorClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsState()
    val viewState by rememberSaveable { state }
    val context = LocalContext.current
    // specificaly checking is needed
    if (viewState == null || viewState.authors.isNullOrEmpty()) {
        LaunchedEffect(key1 = Unit, block = {
            viewModel.submitAction(AuthorsListIntents.RetrieveAuthors(1, isConnectionOn(context)))
        })
    }


    AuthorsListContent(
        viewState,
        onAuthorClick,

        onRetrieveMore = { hoistedContext ->
            if (viewState.isLoading.not() && viewState.hasMoreData)
                viewModel.submitAction(
                    AuthorsListIntents.RetrieveAuthors(
                        ++viewState.page,
                        isConnectionOn(hoistedContext)
                    )
                )
        },
        onRefresh = {
            refreshFeedIfConnected(context, viewModel)
        },
        modifier
    )
}


private fun refreshFeedIfConnected(
    context: Context,
    viewModel: AuthorsListViewModel
) {
    if (isConnectionOn(context)) viewModel.submitAction(AuthorsListIntents.RefreshScreen)
    else SnackbarManager.showMessage(
        getStringValueOrNull(context, "couldn't refresh") ?: ""
    )
}

@ExperimentalFoundationApi
@Composable
private fun AuthorsListContent(
    viewstate: AuthorsListState,
    onAuthorClick: (Long) -> Unit,
    onRetrieveMore: (Context) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    AppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            viewstate.apply {

                AuthorsList(viewstate.authors, onAuthorClick, onRetrieveMore)
                DestinationBar() {
                    onRefresh.invoke()
                }

                if (error.isNotEmpty()) {
                    SnackbarManager.showMessage(
                        getStringValueOrNull(LocalContext.current, error) ?: error
                    )
                    error = ""
                }
            }

        }
    }
}

@ExperimentalFoundationApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AuthorsList(
    authors: Authors,
    onAuthorClick: (Long) -> Unit,
    onRetrieveMore: (Context) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .padding(top = AppBarHeight)
            .background(AppTheme.colors.background)
            .fillMaxWidth()
    ) {

        val listState = rememberLazyListState()
        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier,
            listState, PaddingValues(start = 12.dp, end = 12.dp)
        ) {

            itemsIndexed(authors) { _, snack ->
                AuthorItem(
                    snack,
                    onAuthorClick
                )
            }
        }

        // observer when reached end of list to invoke call to next page
        val shouldLoadMore by remember {
            derivedStateOf {
                listState.isScrolledToEnd()
            }
        }
        if (shouldLoadMore) onRetrieveMore.invoke(LocalContext.current)

    }
}


@Composable
private fun AuthorItem(
    author: Author,
    onAuthorClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    AppCard(
        modifier = modifier
            .size(
                width = 150.dp,
                height = 250.dp
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = { onAuthorClick(author.id.toLong()) })
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {


                RoundImage(
                    imageUrl = author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .padding(4.dp)
                        .align(Alignment.Center)
                )
            }

            Text(
                text = author.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1.merge(TextStyle(fontSize = 18.sp)),
                color = AppTheme.colors.secondary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 8.dp)
            )
            sV(h = 8)
            Text(
                text = "@${author.userName}",
                style = MaterialTheme.typography.subtitle1.merge(TextStyle(fontSize = 14.sp)),
                color = AppTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 8.dp)
            )
            sV(h = 8)

        }
    }
}


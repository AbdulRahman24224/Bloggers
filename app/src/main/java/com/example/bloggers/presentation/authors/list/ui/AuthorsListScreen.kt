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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloggers.base.ui.components.AppCard
import com.example.bloggers.base.ui.components.AppSurface
import com.example.bloggers.base.ui.components.RoundImage
import com.example.bloggers.base.ui.theme.AppTheme
import com.example.bloggers.base.utils.network.ConnectivityUtil.isConnectionOn
import com.example.bloggers.base.utils.ui.isScrolledToEnd
import com.example.bloggers.base.utils.ui.sH
import com.example.bloggers.domain.data.remote.Authors
import com.example.bloggers.entities.Author
import com.example.bloggers.entities.AuthorsListState
import com.example.bloggers.presentation.authors.list.AuthorsListIntents
import com.example.bloggers.presentation.authors.list.AuthorsListViewModel


@ExperimentalFoundationApi
@Composable
fun AuthorsListScreen(
    viewModel: AuthorsListViewModel = hiltViewModel(),
    onAuthorClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.liveData.observeAsState(AuthorsListState())
    val viewState by rememberSaveable { state }

    // specificaly checking is needed
    if (viewState == null || viewState.authors.isNullOrEmpty())
    {
        val context = LocalContext.current
        LaunchedEffect(key1 = Unit, block = {
            viewModel.submitAction(AuthorsListIntents.RetrieveAuthors(1 , isConnectionOn(context ) ))
        })
    }


    AuthorsListContent(
        viewState,
        onAuthorClick,
        onRetrieveMore = { context ->
            if (viewState.isLoading.not() && viewState.hasMoreData)
            viewModel.submitAction(
                AuthorsListIntents.RetrieveAuthors(
                    ++viewState.page ,
                    isConnectionOn(context )))
            println(viewState.page)
        }
        ,
        modifier
    )
}

@ExperimentalFoundationApi
@Composable
private fun AuthorsListContent(
    viewstate: AuthorsListState,
    onAuthorClick: (Long) -> Unit,
    onRetrieveMore: (Context) -> Unit,
    modifier: Modifier = Modifier
) {

    AppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            AuthorsList(viewstate.authors, onAuthorClick , onRetrieveMore )
            DestinationBar()
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
    Box(modifier
        .background(AppTheme.colors.background)
        .fillMaxWidth()) {

        val listState = rememberLazyListState()
        LazyVerticalGrid(
            GridCells.Fixed(2),
             modifier,
            listState
           , PaddingValues(start = 24.dp, end = 24.dp)
        ) {

            itemsIndexed(authors) { index, snack ->
                AuthorItem(
                    snack,
                    onAuthorClick,
                    index
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
    index: Int,

    modifier: Modifier = Modifier
) {

    AppCard(
        modifier = modifier
            .size(
                width = 150.dp,
                height = 280.dp
            )
            .padding( 8.dp)
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

                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
                RoundImage(
                    imageUrl = author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            sH(x = 8)
            Text(
                text = author.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6,
                color = AppTheme.colors.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            sH(x = 8)
            Text(
                text = author.email,
                style = MaterialTheme.typography.body1,
                color = AppTheme.colors.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


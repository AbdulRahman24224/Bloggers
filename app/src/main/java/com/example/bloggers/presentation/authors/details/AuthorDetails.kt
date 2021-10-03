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

package com.example.bloggers.presentation.authors.details


import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloggers.R
import com.example.bloggers.base.ui.components.*
import com.example.bloggers.base.ui.theme.AppTheme
import com.example.bloggers.base.ui.theme.Neutral8
import com.example.bloggers.base.utils.network.ConnectivityUtil
import com.example.bloggers.base.utils.ui.isScrolledToEnd
import com.example.bloggers.base.utils.ui.sH
import com.example.data.entities.Author
import com.example.data.entities.Post
import com.example.domain.usecases.authors.states.AuthorsProfileState
import com.google.accompanist.insets.statusBarsPadding

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun AuthorProfile(
    viewModel : AuthorsProfileViewModel = hiltViewModel() ,
    authorId: Long,
    upPress: () -> Unit
) {

    val state = viewModel.liveData.observeAsState(AuthorsProfileState())
    val viewState by rememberSaveable { state }

    if (viewState == null || viewState.posts.isNullOrEmpty())
    {
        val context = LocalContext.current
        LaunchedEffect(key1 = Unit, block = {
            viewModel.submitAction(AuthorsProfileIntents.RetrieveAuthorData(authorId.toInt() , ConnectivityUtil.isConnectionOn(context ) ))
        })
    }
    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body( viewState.posts, scroll ,
            onRetrieveMore =
            { context ->
                if (viewState.isLoading.not() && viewState.hasMoreData)
                    viewModel.submitAction(
                        AuthorsProfileIntents.RetrieveAuthorPosts(
                            ++viewState.page,
                            ConnectivityUtil.isConnectionOn(context)
                        )
                    )
                println(viewState.page)
            }
        )
        Title(viewState.author, scroll.value)
        Image(viewState.author.avatarUrl, scroll.value)
        Up(upPress)
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(AppTheme.gradient))
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
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
private fun Body(
    posts: List<Post>,
    scroll: ScrollState,
    onRetrieveMore: (Context) -> Unit,
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(

        ) {
            Spacer(Modifier.height(GradientScroll))
            AppSurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                     sH(x = 16)

                    Text(
                        text = stringResource(R.string.posts),
                        style = MaterialTheme.typography.body1,
                        color = AppTheme.colors.secondary,
                        modifier = HzPadding
                    )

                   sH(x = 16)
                    AppDivider()

                    val listState = rememberLazyListState()
                    // observer when reached end of list to invoke call to next page

                            LazyColumn(
                                state = listState
                            ) {

                                itemsIndexed(posts) { index, post ->

                                    if (index > 0) AppDivider()
                                    PostItem(post , {
                                        // todo post click shows comments
                                    })

                                }
                            }

                    val shouldLoadMore by remember {
                        derivedStateOf {
                            listState.isScrolledToEnd()
                        }
                    }
                    if (shouldLoadMore) onRetrieveMore.invoke(LocalContext.current)
                }
            }
        }
    }
}

@Composable
private fun Title(author: Author, scroll: Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .graphicsLayer { translationY = offset }
            .background(color = AppTheme.colors.onBackground)
    ) {
        sH(x = 16)
        Text(
            text = author.name,
            style = MaterialTheme.typography.h4,
            color = AppTheme.colors.onSecondary,
            modifier = HzPadding
        )
        Text(
            text = author.email,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 20.sp,
            color = AppTheme.colors.primaryVariant,
            modifier = HzPadding
        )
        sH(x = 4)
        Text(
            text = author.userName,
            style = MaterialTheme.typography.h6,
            color = AppTheme.colors.primaryVariant,
            modifier = HzPadding
        )

       sH(x = 8)
        AppDivider()
    }
}

@Composable
private fun PostItem(
    post: Post,
    onPostClick: (Long) -> Unit,
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
                .clickable(onClick = { onPostClick(post.id.toLong()) })
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
                    imageUrl = post.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            sH(x = 8)
            Text(
                text = post.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6,
                color = AppTheme.colors.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            sH(x = 8)
            Text(
                text = post.body,
                style = MaterialTheme.typography.body1,
                color = AppTheme.colors.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun Image(
    imageUrl: String,
    scroll: Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)

    CollapsingImageLayout(
         coordinates = CollapsingImageCoordinates(  ExpandedImageSize , CollapsedImageSize , MinImageOffset ,MinTitleOffset ),
        collapseFraction = collapseFraction,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        RoundImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}




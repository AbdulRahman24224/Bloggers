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

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloggers.R
import com.example.bloggers.base.ui.components.*
import com.example.bloggers.base.ui.theme.AppTheme
import com.example.bloggers.base.ui.theme.Neutral8
import com.example.bloggers.base.utils.network.ConnectivityUtil
import com.example.bloggers.base.utils.ui.sH
import com.example.bloggers.entities.Author
import com.example.bloggers.entities.AuthorsProfileState
import com.example.bloggers.entities.Post
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
        val posts = listOf<Post>(Post(title = "aaa") , Post(title = "bb") ,Post(title = "cc"))
        Body( posts, scroll)
        Title( viewState.author,scroll.value)
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
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
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

                    posts.forEach { post ->
                        key(post.id) {

                         /*   LazyColumn(
                                snackCollection = snackCollection,
                                onSnackClick = { },
                                highlight = false
                            )*/
                        }
                    }


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




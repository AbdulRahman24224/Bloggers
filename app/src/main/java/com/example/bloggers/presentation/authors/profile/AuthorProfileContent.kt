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

package com.example.bloggers.presentation.authors.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.bloggers.R
import com.example.bloggers.base.ui.components.*
import com.example.bloggers.base.ui.theme.AppTheme
import com.example.bloggers.base.utils.SnackbarManager
import com.example.bloggers.base.utils.datetime.DateTimeUtil.getDateTime
import com.example.bloggers.base.utils.network.ConnectivityUtil
import com.example.bloggers.base.utils.network.ConnectivityUtil.isConnectionOn
import com.example.bloggers.base.utils.resources.StringResourcesUtil
import com.example.bloggers.base.utils.ui.isScrolledToEnd
import com.example.bloggers.base.utils.ui.sP
import com.example.bloggers.base.utils.ui.sV
import com.example.data.entities.Address
import com.example.data.entities.Author
import com.example.data.entities.Post
import com.google.accompanist.insets.statusBarsPadding
import java.util.*


private val ProfileCardHeight = 220.dp

@Composable
fun AuthorProfileScreen(
    viewModel: AuthorsProfileViewModel = hiltViewModel(),
    authorId: Long,
    upPress: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val viewState by rememberSaveable { state }
    val context = LocalContext.current

    if (viewState == null || viewState.posts.isNullOrEmpty()) {
        LaunchedEffect(key1 = Unit, block = {
            viewModel.submitAction(
                AuthorsProfileIntents.RetrieveAuthorData(
                    authorId.toInt(),
                    ConnectivityUtil.isConnectionOn(context)
                )
            )
        })
    }

    AppSurface(modifier = Modifier.fillMaxSize()) {
        Box {

            viewState.apply {

                AuthorPosts(
                    author,
                    posts,
                    onRetrieveMore =
                    { context ->
                        if (isLoading.not() && hasMoreData)
                            viewModel.submitAction(
                                AuthorsProfileIntents.RetrieveAuthorPosts(
                                    ++page,
                                    ConnectivityUtil.isConnectionOn(context)
                                )
                            )
                    })

                AuthorCard(author){
                    val uri: String = java.lang.String.format(
                        Locale.ENGLISH,
                        "geo:%f,%f",
                        author.address.latitude,
                        author.address.longitude
                    )
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    context.startActivity(intent)
                }

                Row() {
                    BackButton(modifier = Modifier.weight(1f)) { upPress.invoke() }
                    Spacer(modifier = Modifier.weight(4f))
                    RefreshButton (modifier = Modifier.weight(1f)){

                   viewModel.submitAction(
                       AuthorsProfileIntents.RefreshScreen(isConnectionOn(context))) }
                }

                if (error.isNotEmpty()) {
                    SnackbarManager.showMessage(
                        StringResourcesUtil.getStringValueOrNull(LocalContext.current, error) ?: error
                    )
                    error = ""
                }

            }


        }
    }


}

@Composable
private fun AuthorCard(author: Author , onAddressClicked: (Address) -> Unit) {
    Surface(
        color = AppTheme.colors.onSecondary,
        shape = RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp),
        modifier = Modifier
            .requiredHeight(ProfileCardHeight)
            .fillMaxWidth()
            .background(Color.Transparent)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .statusBarsPadding()
        ) {
            sV(h = 40)

            Row(modifier = Modifier.fillMaxWidth()) {

                RoundImage(
                    imageUrl = author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 8.dp)
                        .width(150.dp)
                        .height(150.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                )
                {
                    sV(h = 25)
                    Text(
                        text = author.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1
                            .merge(TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)),
                        color = AppTheme.colors.secondary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                    sV(h = 8)
                    Text(
                        text = "@${author.userName}",
                        style = MaterialTheme.typography.subtitle1.merge(TextStyle(fontSize = 14.sp)),
                        color = AppTheme.colors.primary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                    sV(h = 8)
                    Text(
                        text = "@${author.email}",
                        style = MaterialTheme.typography.subtitle1.merge(TextStyle(fontSize = 12.sp)),
                        color = AppTheme.colors.primary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    IconButton(

                        onClick = { onAddressClicked.invoke(author.address)}
                        ,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(horizontal = 8.dp),

                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            tint = AppTheme.colors.primary,
                            contentDescription = stringResource(R.string.our_authors)
                        )
                    }
                }

            }

        }
    }
}


@Composable
private fun AuthorPosts(
    author: Author,
    posts: List<Post>,
    onRetrieveMore: (Context) -> Unit,
) {

    Surface(
        color = AppTheme.colors.background,
        shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {

        val listState = rememberLazyListState()
        // observer when reached end of list to invoke call to next page

        LazyColumn(
            modifier = Modifier.padding(top = ProfileCardHeight),
            state = listState
        ) {

            itemsIndexed(posts) { index, post ->

                if (index > 0) AppDivider()
                PostItem(author, post, {
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


@Composable
fun PostItem(author: Author, post: Post, onPostClick: (Long) -> Unit) {

    AppCard(modifier = Modifier.padding(12.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = { onPostClick(post.id.toLong()) })
        ) {

            Row(modifier = Modifier.padding(8.dp)) {

                RoundImage(
                    imageUrl = author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 4.dp)
                        .width(35.dp)
                        .height(35.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = author.name, fontWeight = FontWeight.Bold)

                    Text(
                        text = getDateTime(post.date) ?: "",
                        color = AppTheme.colors.onSurface,
                        fontSize = 12.sp
                    )
                }

            }

            Text(text = post.body, modifier = Modifier.padding(8.dp))
            sP(x = 8)
            Image(
                painter = rememberImagePainter(
                    data = post.imageUrl,
                    builder = {
                        crossfade(true)
                        placeholder(drawableResId = R.drawable.ic_launcher_foreground)
                        error(drawableResId = R.drawable.ic_launcher_foreground)

                    }
                ),
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        }
    }

}




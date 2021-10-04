package com.example.bloggers.base.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.bloggers.presentation.authors.list.ui.AuthorsListScreen
import com.example.bloggers.presentation.authors.profile.AuthorProfileScreen


@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun NavGraphBuilder.AppNavGraph(
    onAuthorSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = MainDestinations.AUTHOR_LIST_ROUTE
    ) {
        composable(MainDestinations.AUTHOR_LIST_ROUTE) {

            AuthorsListScreen(onAuthorClick = { id ->
                onAuthorSelected.invoke(id, it)
            })

        }
    }
    composable(
        "${MainDestinations.AUTHOR_DETAIL_ROUTE}/{${MainDestinations.AUTHOR_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.AUTHOR_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val authorId = arguments.getLong(MainDestinations.AUTHOR_ID_KEY)

        AuthorProfileScreen(authorId = authorId, upPress = upPress)
    }
}
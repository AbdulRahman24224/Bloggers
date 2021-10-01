package com.example.bloggers.base.ui

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.bloggers.presentation.authors.list.ui.AuthorsListScreen


fun NavGraphBuilder.AppNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = MainDestinations.AUTHOR_LIST_ROUTE
    ) {
        composable(MainDestinations.AUTHOR_LIST_ROUTE) {
            AuthorsListScreen(onAuthorClick = {id ->

            })
        }
    }
    composable(
        "${MainDestinations.AUTHOR_DETAIL_ROUTE}/{${MainDestinations.AUTHOR_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.AUTHOR_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.AUTHOR_ID_KEY)

        // SnackDetail(snackId, upPress)
    }
}
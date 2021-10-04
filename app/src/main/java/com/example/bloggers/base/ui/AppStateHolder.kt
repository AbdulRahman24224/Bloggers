package com.example.bloggers.base.ui

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bloggers.base.utils.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


object MainDestinations {
    const val HOME_ROUTE = "home"
    const val AUTHOR_LIST_ROUTE = "home/authorsList"
    const val AUTHOR_DETAIL_ROUTE = "home/authorsList/author"
    const val AUTHOR_ID_KEY = "authorId"
}

/**
 * Remembers and creates an instance of [AppStateHolder]
 */
@Composable
fun rememberAppStateHolder(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        AppStateHolder(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }


@Stable
class AppStateHolder(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {

    init {
        coroutineScope.launch {
            snackbarManager.messages
                .collect { currentMessages ->
                    if (currentMessages.isNotEmpty()) {
                        val newMessage = currentMessages[0]
                        val text = newMessage.message

                        // Display the snackbar on the screen. `showSnackbar` is a function
                        // that suspends until the snackbar disappears from the screen
                        scaffoldState.snackbarHostState.showSnackbar(text)
                        // Once the snackbar is gone or dismissed, notify the SnackbarManager
                        snackbarManager.setMessageShown(newMessage.id)
                    }
                }
        }
    }


    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }


    fun navigateToAuthorDetails(authorId: Long, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.AUTHOR_DETAIL_ROUTE}/$authorId")
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
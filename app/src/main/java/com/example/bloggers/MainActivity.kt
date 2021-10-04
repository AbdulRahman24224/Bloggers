package com.example.bloggers

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import com.example.bloggers.base.ui.AppNavGraph
import com.example.bloggers.base.ui.MainDestinations
import com.example.bloggers.base.ui.components.AppScaffold
import com.example.bloggers.base.ui.rememberAppStateHolder
import com.example.bloggers.base.ui.theme.AppTheme
import com.example.bloggers.base.ui.theme.BloggersTheme
import com.google.accompanist.insets.systemBarsPadding
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BloggersTheme {

                val appStateHolder = rememberAppStateHolder()

                Surface(color = AppTheme.colors.background) {

                    AppScaffold(
                        snackbarHost = {
                            SnackbarHost(
                                hostState = it,
                                modifier = Modifier.systemBarsPadding(),
                                snackbar = { snackbarData -> Snackbar(snackbarData) }
                            )
                        },
                        scaffoldState = appStateHolder.scaffoldState
                    )
                    { innerPaddingModifier ->

                        NavHost(
                            navController = appStateHolder.navController,
                            startDestination = MainDestinations.HOME_ROUTE,
                            modifier = Modifier.padding(innerPaddingModifier)
                        ) {
                            AppNavGraph(
                                onAuthorSelected = appStateHolder::navigateToAuthorDetails,
                                upPress = appStateHolder::upPress
                            )
                        }
                    }

                }
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}
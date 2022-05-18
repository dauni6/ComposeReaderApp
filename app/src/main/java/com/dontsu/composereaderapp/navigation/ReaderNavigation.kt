package com.dontsu.composereaderapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dontsu.composereaderapp.screens.create_acount.ReaderCreateAccountScreen
import com.dontsu.composereaderapp.screens.detail.ReaderBookDetailScreen
import com.dontsu.composereaderapp.screens.home.ReaderHomeScreen
import com.dontsu.composereaderapp.screens.login.ReaderLoginScreen
import com.dontsu.composereaderapp.screens.search.ReaderSearchScreen
import com.dontsu.composereaderapp.screens.splash.ReaderSplashScreen
import com.dontsu.composereaderapp.screens.stats.ReaderStatsScreen
import com.dontsu.composereaderapp.screens.update.ReaderUpdateScreen

@ExperimentalComposeUiApi
@Composable
fun ReaderNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReaderScreens.ReaderSplashScreen.name
    ) {
        composable(route = ReaderScreens.ReaderSplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderLoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderCreateAccountScreen.name) {
            ReaderCreateAccountScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderHomeScreen.name) {
            ReaderHomeScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderBookSearchScreen.name) {
            ReaderSearchScreen(navController = navController)
        }

        composable(
            route = ReaderScreens.ReaderBookDetailScreen.name + "/{bookId}",
            arguments = listOf(navArgument(
                "bookId"
            ) {
                type = NavType.StringType
              }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId")?.let {
                ReaderBookDetailScreen(navController = navController, bookId = it)
            }
        }

        composable(
            route = ReaderScreens.ReaderUpdateScreen.name + "/{bookItemId}",
            arguments = listOf(
                navArgument(
                    "bookItemId"
                ) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookItemId")?.let {
                Log.d("TEST", "navigation : $it")
                ReaderUpdateScreen(navController = navController, bookItemId = it)
            }
        }

        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }

    }

}

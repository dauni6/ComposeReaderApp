package com.dontsu.composereaderapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dontsu.composereaderapp.screens.create_acount.ReaderCreateAccountScreen
import com.dontsu.composereaderapp.screens.detail.ReaderBookDetailScreen
import com.dontsu.composereaderapp.screens.home.ReaderHomeScreen
import com.dontsu.composereaderapp.screens.login.ReaderLoginScreen
import com.dontsu.composereaderapp.screens.splash.ReaderSplashScreen
import com.dontsu.composereaderapp.screens.stats.ReaderStatsScreen
import com.dontsu.composereaderapp.screens.update.ReaderUpdateScreen

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
            ReaderBookDetailScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderBookDetailScreen.name) {
            ReaderBookDetailScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderUpdateScreen.name) {
            ReaderUpdateScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }

    }

}

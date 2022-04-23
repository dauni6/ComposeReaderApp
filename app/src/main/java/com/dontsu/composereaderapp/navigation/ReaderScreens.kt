package com.dontsu.composereaderapp.navigation

enum class ReaderScreens {

    ReaderSplashScreen,
    ReaderLoginScreen,
    ReaderCreateAccountScreen,
    ReaderHomeScreen,
    ReaderBookSearchScreen,
    ReaderBookDetailScreen,
    ReaderUpdateScreen,
    ReaderStatsScreen;

    companion object {
        fun fromRoute(route: String?): ReaderScreens =
            when (route?.substringBefore("/")) {
                ReaderSplashScreen.name -> ReaderSplashScreen
                ReaderLoginScreen.name -> ReaderLoginScreen
                ReaderCreateAccountScreen.name -> ReaderCreateAccountScreen
                ReaderHomeScreen.name -> ReaderHomeScreen
                ReaderBookSearchScreen.name -> ReaderBookSearchScreen
                ReaderBookDetailScreen.name -> ReaderBookDetailScreen
                ReaderUpdateScreen.name -> ReaderUpdateScreen
                ReaderStatsScreen.name -> ReaderStatsScreen
                null -> ReaderHomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }

}

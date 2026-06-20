package com.aneesh.echo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.aneesh.echo.ui.detail.StoryDetailsRoute
import com.aneesh.echo.ui.storyList.StoryListRoute

@Composable
fun EchoNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ListScreen,
        modifier = modifier
    ) {
        composable<ListScreen> {
            StoryListRoute(onStoryClick = { id ->
                navController.navigate(DetailScreen(id))
            })
        }

        composable<DetailScreen>(
            deepLinks = listOf(
                navDeepLink<DetailScreen>(basePath = "echo://story")
            )
        ) {
            StoryDetailsRoute()
        }
    }
}
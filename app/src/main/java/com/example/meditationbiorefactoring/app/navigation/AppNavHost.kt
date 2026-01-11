package com.example.meditationbiorefactoring.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.meditationbiorefactoring.app.presentation.HomeScreen
import com.example.meditationbiorefactoring.bio.presentation.bio_history.BioHistoryScreen
import com.example.meditationbiorefactoring.bio.presentation.measurement.bpm.BpmScreen
import com.example.meditationbiorefactoring.bio.presentation.measurement.brpm.BrpmScreen
import com.example.meditationbiorefactoring.bio.presentation.measurement.siv.SivScreen
import com.example.meditationbiorefactoring.music.presentation.MusicScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                onNavigateToBpm = { navController.navigate(Screen.BpmScreen.route) },
                onNavigateToMusic = { navController.navigate(Screen.MusicScreen.createRoute()) },
                onNavigateToBioHistory = { navController.navigate(Screen.BioHistoryScreen.route) }
            )
        }
        composable(route = Screen.BpmScreen.route) {
            BpmScreen(
                onNavigateToBrpm = { navController.navigate(Screen.BrpmScreen.route) }
            )
        }
        composable(route = Screen.BrpmScreen.route) {
            BrpmScreen(
                onNavigateToSiv = { navController.navigate(Screen.SivScreen.route) }
            )
        }
        composable(route = Screen.SivScreen.route) {
            SivScreen(
                onNavigateToMusic = { stressLevel ->
                    navController.navigate(
                        Screen.MusicScreen.createRoute(stressLevel = stressLevel)
                    )
                }
            )
        }
        composable(route = Screen.BioHistoryScreen.route) {
            BioHistoryScreen(
                onNavigateToMusic = { stressLevel ->
                    navController.navigate(
                        Screen.MusicScreen.createRoute(stressLevel = stressLevel)
                    )
                }
            )
        }
        composable(route = Screen.MusicScreen.route,
            arguments = listOf(
                navArgument("stressLevel") {
                    type = NavType.StringType
                    defaultValue = "none"
                }
            )
        ) { backStackEntry ->
            val stressLevel = backStackEntry.arguments?.getString("stressLevel")

            MusicScreen(
                stressLevel = if (stressLevel == "none") null else stressLevel,
            )

            BackHandler {
                navController.popBackStack(
                    route = Screen.HomeScreen.route,
                    inclusive = false
                )
            }
        }
    }
}
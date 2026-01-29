package com.example.skillforge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.skillforge.ui.screens.FlashcardScreen
import com.example.skillforge.ui.screens.HomeScreen

object Routes {
    const val HOME = "home"
    const val FLASHCARD_STUDY = "flashcard_study/{skillId}"

    fun flashcardStudy(skillId: Long) = "flashcard_study/$skillId"
}

@Composable
fun SkillForgeNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onSkillClick = { skillId ->
                    navController.navigate(Routes.flashcardStudy(skillId))
                }
            )
        }

        composable(
            route = Routes.FLASHCARD_STUDY,
            arguments = listOf(
                navArgument("skillId") { type = NavType.LongType }
            )
        ) {
            FlashcardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

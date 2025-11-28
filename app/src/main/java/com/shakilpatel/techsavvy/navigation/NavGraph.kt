package com.shakilpatel.techsavvy.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.shakilpatel.techsavvy.data.repository.VersionRepository
import com.shakilpatel.techsavvy.ui.screens.*
import com.shakilpatel.techsavvy.ui.screens.auth.LoginScreen
import com.shakilpatel.techsavvy.ui.screens.home.HomeScreen
import com.shakilpatel.techsavvy.ui.screens.programs.ProgramDashboardScreen
import com.shakilpatel.techsavvy.ui.screens.programs.ProgramsListScreen
import com.shakilpatel.techsavvy.ui.screens.profile.EditProfileScreen
import com.shakilpatel.techsavvy.ui.screens.profile.ProfileScreen
import com.shakilpatel.techsavvy.ui.screens.programs.BrowseProgramsScreen
import com.shakilpatel.techsavvy.ui.screens.settings.SettingsScreen
import com.shakilpatel.techsavvy.ui.theme.AppTheme

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Login : Screen("login")
    object ProgramsList : Screen("programs_list")
    object ProgramDashboard : Screen("program_dashboard/{programId}") {
        fun createRoute(programId: String) = "program_dashboard/$programId"
    }
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Settings : Screen("settings")
    object BrowsePrograms : Screen("browse_programs") // ADD THIS

}

@Composable
fun NavGraph(
    navController: NavHostController,
    versionRepository: VersionRepository, // ADD THIS
    startDestination: String = Screen.Splash.route,
    onThemeChange: (AppTheme, Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                versionRepository = versionRepository // PASS HERE
            )
        }


        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel = hiltViewModel())
        }

        composable(Screen.ProgramsList.route) {
            ProgramsListScreen(navController = navController, authViewModel = hiltViewModel(), programViewModel = hiltViewModel())
        }

        composable(
            route = Screen.ProgramDashboard.route,
            arguments = listOf(navArgument("programId") { type = NavType.StringType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getString("programId") ?: ""
            ProgramDashboardScreen(
                navController = navController,
                programId = programId,
                authViewModel = hiltViewModel(),
                programViewModel = hiltViewModel()
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, authViewModel = hiltViewModel())
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController, authViewModel = hiltViewModel())
        }
        composable(route = Screen.BrowsePrograms.route) {
            BrowseProgramsScreen(navController = navController)
        }


        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController, onThemeChange =onThemeChange )
        }
    }
}

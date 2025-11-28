package com.shakilpatel.techsavvy.navigation.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shakilpatel.techsavvy.navigation.AppScreen
import com.shakilpatel.techsavvy.ui.profile.ProfileScreen

fun NavGraphBuilder.ProfileNav(navController: NavController){
    navigation(startDestination = AppScreen.Profile.Landing.route, route = AppScreen.Profile.route){
        composable(route = AppScreen.Profile.Landing.route){
            ProfileScreen(hiltViewModel(),navController = navController)
        }
    }
}
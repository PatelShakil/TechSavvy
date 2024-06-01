package com.shakilpatel.techsavvy.nav.navigations

import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shakilpatel.techsavvy.nav.AppScreen
import com.shakilpatel.techsavvy.ui.auth.LoginScreen
import com.shakilpatel.techsavvy.ui.home.HomeScreen
import com.shakilpatel.techsavvy.ui.profile.ProfileScreen

fun NavGraphBuilder.ProfileNav(navController: NavController){
    navigation(startDestination = AppScreen.Profile.Landing.route, route = AppScreen.Profile.route){
        composable(route = AppScreen.Profile.Landing.route){
            ProfileScreen(hiltViewModel(),navController = navController)
        }
    }
}
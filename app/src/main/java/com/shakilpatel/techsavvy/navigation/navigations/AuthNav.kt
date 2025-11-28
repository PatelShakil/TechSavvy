package com.shakilpatel.techsavvy.navigation.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shakilpatel.techsavvy.navigation.AppScreen
import com.shakilpatel.techsavvy.ui.auth.LoginScreen

fun NavGraphBuilder.AuthNav(navController: NavController){
    navigation(startDestination = AppScreen.Auth.Login.route, route = AppScreen.Auth.route){
        composable(route = AppScreen.Auth.Login.route){
            LoginScreen(hiltViewModel(),navController = navController)
        }
    }
}
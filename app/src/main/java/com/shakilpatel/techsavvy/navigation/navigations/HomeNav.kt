package com.shakilpatel.techsavvy.navigation.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shakilpatel.techsavvy.navigation.AppScreen
//import com.shakilpatel.techsavvy.ui.home.HomeScreen

fun NavGraphBuilder.HomeNav(navController: NavController){
    navigation(startDestination = AppScreen.Home.Landing.route, route = AppScreen.Home.route){
        composable(route = AppScreen.Home.Landing.route){
//            HomeScreen(hiltViewModel(),navController = navController)
        }
    }
}
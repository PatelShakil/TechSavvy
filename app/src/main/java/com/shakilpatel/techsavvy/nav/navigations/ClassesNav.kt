package com.shakilpatel.techsavvy.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shakilpatel.techsavvy.nav.AppScreen
import com.shakilpatel.techsavvy.ui.classes.ClassesScreen

fun NavGraphBuilder.ClassesNav(navController: NavController){
    navigation(startDestination = AppScreen.Classes.Landing.route, route = AppScreen.Classes.route){
        composable(route = AppScreen.Classes.Landing.route){
            ClassesScreen(hiltViewModel(),navController = navController)
        }
    }
}
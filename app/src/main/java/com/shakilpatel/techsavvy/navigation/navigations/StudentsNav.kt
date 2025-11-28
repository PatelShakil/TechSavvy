package com.shakilpatel.techsavvy.navigation.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.shakilpatel.techsavvy.navigation.AppScreen
import com.shakilpatel.techsavvy.ui.students.StudentsScreen

fun NavGraphBuilder.StudentsNav(navController: NavController){
    navigation(startDestination = AppScreen.Students.Landing.route + "/{gender}",
        route = AppScreen.Students.route + "/{gender}",
        arguments = listOf(
            navArgument("gender"){
                type = NavType.StringType
            }
        )){
        composable(route = AppScreen.Students.Landing.route + "/{gender}",
            arguments = listOf(
                navArgument("gender"){
                    type = NavType.StringType
                }
            )
        ){
            if(it.arguments!=null){
                val gender = it.arguments?.getString("gender")
                if(gender!=null){
                    StudentsScreen(gender,hiltViewModel(),navController)
                }
            }
        }
    }
}
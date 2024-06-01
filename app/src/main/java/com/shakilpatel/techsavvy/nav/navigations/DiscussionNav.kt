package com.shakilpatel.techsavvy.nav.navigations

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shakilpatel.techsavvy.nav.AppScreen
import com.shakilpatel.techsavvy.ui.discussion.DiscussionScreen

fun NavGraphBuilder.DiscussionNav(navController: NavController){
    navigation(startDestination = AppScreen.Discussion.Landing.route, route = AppScreen.Discussion.route){
        composable(route = AppScreen.Discussion.Landing.route){
            DiscussionScreen(hiltViewModel(),navController = navController)
        }
    }
}
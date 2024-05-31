package com.shakilpatel.techsavvy.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.shakilpatel.techsavvy.nav.navigations.AuthNav

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Auth.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            AuthNav(navController = navController)
        }
    }


}
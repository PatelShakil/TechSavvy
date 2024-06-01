package com.shakilpatel.techsavvy.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.nav.navigations.AuthNav
import com.shakilpatel.techsavvy.nav.navigations.HomeNav
import com.shakilpatel.techsavvy.nav.navigations.ProfileNav

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Image(
        painterResource(id = R.drawable.home_bg),
        "",
        modifier= Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
                AppBar(navController = navController)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Auth.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            AuthNav(navController = navController)
            HomeNav(navController)
            ProfileNav(navController)
        }
    }



}
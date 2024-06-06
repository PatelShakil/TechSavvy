package com.shakilpatel.techsavvy.nav

import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
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
import com.shakilpatel.techsavvy.nav.navigations.ClassesNav
import com.shakilpatel.techsavvy.nav.navigations.DiscussionNav
import com.shakilpatel.techsavvy.nav.navigations.HomeNav
import com.shakilpatel.techsavvy.nav.navigations.ProfileNav
import com.shakilpatel.techsavvy.nav.navigations.StudentsNav

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Image(
        painterResource(id = R.drawable.home_bg_1),
        "",
        modifier= Modifier.fillMaxSize()
            .imePadding(),
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
            DiscussionNav(navController)
            ClassesNav(navController)
            StudentsNav(navController)
        }
    }
}
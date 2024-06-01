package com.shakilpatel.techsavvy.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shakilpatel.techsavvy.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }

    when(navController.currentBackStackEntryAsState().value?.destination?.parent?.route){
        AppScreen.Home.route->{
            isVisible = true
        }
        AppScreen.Profile.route->{
            isVisible = true
        }
        else->{
            isVisible = false
        }
    }
    var rotate by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(key1 = true) {
        while(true){
            if(rotate == 360f){
                rotate = 0f
            }else{
                delay(50)
                rotate += 5
            }
        }
    }


    Column(modifier= Modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = isVisible) {
            TopAppBar(title = { Text("Tech Savvy") },
                navigationIcon = {
                    Image(
                        painterResource(id = R.drawable.logo_gr_tr),
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(46.dp)
                            .rotate(rotate)
                    )
                },
                actions = {
                    AnimatedVisibility(visible = navController.currentBackStackEntryAsState().value?.destination?.parent?.route != AppScreen.Profile.route) {
                        IconButton(onClick = {
                            navController.navigate(AppScreen.Profile.route)
                        }) {
                            Icon(
                                Icons.Default.AccountCircle,
                                "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}
package com.shakilpatel.techsavvy.ui.profile

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ProfileScreen(viewModel : ProfileViewModel,navController: NavController) {
    Surface {
        Text("Profile")
    }
}
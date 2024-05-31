package com.shakilpatel.techsavvy.ui.auth

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun LoginScreen(viewModel : AuthViewModel,navController: NavController) {
    Surface {
        Text(text = "Login Screen")
    }
}
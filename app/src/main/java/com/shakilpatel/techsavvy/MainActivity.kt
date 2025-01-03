package com.shakilpatel.techsavvy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shakilpatel.techsavvy.nav.AppNavHost
import com.shakilpatel.techsavvy.ui.home.TechSavvyWebHome
import com.shakilpatel.techsavvy.ui.theme.TechSavvyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TechSavvyTheme {
//                AppNavHost()
            TechSavvyWebHome()
            }
        }
    }
}
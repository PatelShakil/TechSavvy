package com.shakilpatel.techsavvy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.shakilpatel.techsavvy.nav.AppNavHost
import com.shakilpatel.techsavvy.ui.home.TechSavvyWebHome
import com.shakilpatel.techsavvy.ui.theme.TechSavvyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var filePathCallback: ValueCallback<Array<Uri>>? = null

    // Register a launcher with the new Activity Result API to handle file chooser intents.
    lateinit var fileChooserLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Register the file chooser launcher.
        fileChooserLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val uri: Uri? = result.data?.data
                    if (uri != null) {
                        filePathCallback?.onReceiveValue(arrayOf(uri))
                    } else {
                        filePathCallback?.onReceiveValue(null)
                    }
                } else {
                    filePathCallback?.onReceiveValue(null)
                }
                filePathCallback = null
            }

        enableEdgeToEdge()
        setContent {
            TechSavvyTheme {
//                AppNavHost()
            TechSavvyWebHome()
            }
        }
    }
}
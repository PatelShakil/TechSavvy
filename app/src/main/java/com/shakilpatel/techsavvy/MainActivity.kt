package com.shakilpatel.techsavvy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.shakilpatel.techsavvy.data.preferences.ThemePreferences
import com.shakilpatel.techsavvy.data.repository.VersionConfig
import com.shakilpatel.techsavvy.data.repository.VersionRepository
import com.shakilpatel.techsavvy.navigation.NavGraph
import com.shakilpatel.techsavvy.ui.components.ForceUpdateDialog
import com.shakilpatel.techsavvy.ui.components.OptionalUpdateDialog
import com.shakilpatel.techsavvy.ui.theme.TechSavvyTheme
import com.shakilpatel.techsavvy.utils.VersionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var themePrefs: ThemePreferences

    @Inject
    lateinit var versionRepository: VersionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themePrefs = ThemePreferences(this)

        setContent {
            var currentTheme by remember { mutableStateOf(themePrefs.getTheme()) }
            var isDarkMode by remember { mutableStateOf(themePrefs.isDarkMode()) }

            // Version control states
            var showForceUpdate by remember { mutableStateOf(false) }
            var showOptionalUpdate by remember { mutableStateOf(false) }
            var versionConfig by remember { mutableStateOf<VersionConfig?>(null) }
            var versionCheckCompleted by remember { mutableStateOf(false) }

            // Check version on startup
            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    val result = versionRepository.getVersionConfig()

                    result.onSuccess { config ->
                        versionConfig = config
                        val currentVersion = VersionManager.getCurrentVersion(this@MainActivity)

                        // Check for force update
                        val needsForceUpdate = VersionManager.isUpdateRequired(
                            currentVersion,
                            config.minRequiredVersion
                        )

                        // Check for optional update
                        val hasOptionalUpdate = !needsForceUpdate &&
                                VersionManager.isUpdateAvailable(
                                    currentVersion,
                                    config.latestVersion
                                )

                        showForceUpdate = needsForceUpdate
                        showOptionalUpdate = hasOptionalUpdate && config.forceUpdate
                        versionCheckCompleted = true
                    }

                    result.onFailure {
                        // If version check fails, allow app to continue
                        versionCheckCompleted = true
                    }
                }
            }

            TechSavvyTheme(
                appTheme = currentTheme,
                darkTheme = isDarkMode
            ) {
                // Show force update dialog if required
                if (showForceUpdate && versionConfig != null) {
                    ForceUpdateDialog(
                        title = "Update Required",
                        message = versionConfig!!.criticalUpdateMessage,
                        playStoreUrl = versionConfig!!.playStoreUrl
                    )
                } else {
                    // Show optional update dialog if available
                    if (showOptionalUpdate && versionConfig != null) {
                        OptionalUpdateDialog(
                            title = "Update Available",
                            message = versionConfig!!.updateMessage,
                            playStoreUrl = versionConfig!!.playStoreUrl,
                            onDismiss = { showOptionalUpdate = false }
                        )
                    }

                    // Show app content only after version check completes
                    if (versionCheckCompleted) {
                        val navController = rememberNavController()
                        NavGraph(
                            navController = navController,
                            versionRepository = versionRepository, // PASS HERE
                            onThemeChange = { theme, dark ->
                                currentTheme = theme
                                isDarkMode = dark
                            }
                        )
                    } else {
                        // Show loading screen while checking version
                        Box(
                            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

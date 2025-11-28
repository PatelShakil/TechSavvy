package com.shakilpatel.techsavvy.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.data.repository.VersionConfig
import com.shakilpatel.techsavvy.data.repository.VersionRepository
import com.shakilpatel.techsavvy.navigation.Screen
import com.shakilpatel.techsavvy.ui.components.ForceUpdateDialog
import com.shakilpatel.techsavvy.ui.components.OptionalUpdateDialog
import com.shakilpatel.techsavvy.utils.VersionManager
import kotlinx.coroutines.delay
import javax.inject.Inject

@Composable
fun SplashScreen(
    navController: NavController,
    versionRepository: VersionRepository
) {
    val auth = FirebaseAuth.getInstance()

    // Version control states
    var showForceUpdate by remember { mutableStateOf(false) }
    var showOptionalUpdate by remember { mutableStateOf(false) }
    var versionConfig by remember { mutableStateOf<VersionConfig?>(null) }
    var shouldNavigate by remember { mutableStateOf(false) }

    // Animation
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // Check version and handle navigation
    LaunchedEffect(Unit) {
        delay(1500) // Show splash for at least 1.5 seconds

        // Check version from Firebase
        val result = versionRepository.getVersionConfig()

        result.onSuccess { config ->
            versionConfig = config
            val currentVersion = VersionManager.getCurrentVersion(navController.context)

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

            if (needsForceUpdate) {
                // Show force update dialog - don't navigate
                showForceUpdate = true
            } else if (hasOptionalUpdate && config.forceUpdate) {
                // Show optional update dialog
                showOptionalUpdate = true
                shouldNavigate = true
            } else {
                // No update needed - navigate immediately
                shouldNavigate = true
            }
        }

        result.onFailure {
            // If version check fails, allow app to continue
            shouldNavigate = true
        }
    }

    // Navigate after version check (if allowed)
    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate && !showForceUpdate) {
            delay(500) // Small delay for smooth transition
            val currentUser = auth.currentUser
            if (currentUser != null) {
                navController.navigate(Screen.ProgramsList.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with animation
            Image(
                painter = painterResource(id = R.drawable.logo_gr_tr),
                contentDescription = "Tech Savvy Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Companion.Black)
                    .scale(scale)
                    .alpha(alpha)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // App Name
            Text(
                text = "Tech Savvy",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Solution",
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Loading indicator
            if (!showForceUpdate && !showOptionalUpdate) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp
                )
            }
        }

        // Version text at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "v${VersionManager.getCurrentVersion(navController.context)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        }
    }

    // Show Force Update Dialog if needed
    if (showForceUpdate && versionConfig != null) {
        ForceUpdateDialog(
            title = "Update Required",
            message = versionConfig!!.criticalUpdateMessage,
            playStoreUrl = versionConfig!!.playStoreUrl
        )
    }

    // Show Optional Update Dialog if needed
    if (showOptionalUpdate && versionConfig != null) {
        OptionalUpdateDialog(
            title = "Update Available",
            message = versionConfig!!.updateMessage,
            playStoreUrl = versionConfig!!.playStoreUrl,
            onDismiss = {
                showOptionalUpdate = false
                // Navigation will happen automatically since shouldNavigate is true
            }
        )
    }
}

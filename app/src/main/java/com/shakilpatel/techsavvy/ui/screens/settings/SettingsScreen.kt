package com.shakilpatel.techsavvy.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.datatransport.BuildConfig
import com.shakilpatel.techsavvy.data.preferences.ThemePreferences
import com.shakilpatel.techsavvy.ui.theme.AppTheme
import com.shakilpatel.techsavvy.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onThemeChange: (AppTheme, Boolean) -> Unit
) {
    val context = LocalContext.current
    val themePrefs = remember { ThemePreferences(context) }

    var selectedTheme by remember { mutableStateOf(themePrefs.getTheme()) }
    var darkModeEnabled by remember { mutableStateOf(themePrefs.isDarkMode()) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            item {
                Text(
                    text = "🎨 Appearance",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        // Theme Selection
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showThemeDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Theme",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimary
                                )
                                Text(
                                    text = getThemeName(selectedTheme),
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        }

                        Divider()

                        // Dark Mode Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (darkModeEnabled) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Dark Mode",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (darkModeEnabled) "Enabled" else "Disabled",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Switch(
                                checked = darkModeEnabled,
                                onCheckedChange = { enabled ->
                                    darkModeEnabled = enabled
                                    themePrefs.setDarkMode(enabled)
                                    onThemeChange(selectedTheme, enabled)
                                }
                            )
                        }
                    }
                }
            }

            // Notifications Section
            item {
                Text(
                    text = "🔔 Notifications",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    var notificationsEnabled by remember { mutableStateOf(true) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Push Notifications",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            Text(
                                text = "Receive updates about programs",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }
                }
            }

            // About Section
            item {
                Text(
                    text = "ℹ️ About",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Info,
                            title = "About App",
                            onClick = { showAboutDialog = true }
                        )
                        Divider()
                        val urlHandler = LocalUriHandler.current
                        SettingsItem(
                            icon = Icons.Default.Policy,
                            title = "Privacy Policy",
                            onClick = {
                                //open external url
                                urlHandler.openUri("https://tech-savvy-solution.web.app/apps/tech-savvy/privacy-policy")

                            }
                        )
                        Divider()
                        SettingsItem(
                            icon = Icons.Default.Description,
                            title = "Terms of Service",
                            onClick = {
                                urlHandler.openUri("https://tech-savvy-solution.web.app/apps/tech-savvy/terms-of-service")
                            }
                        )
                    }
                }
            }

            // App Info
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tech Savvy Solution",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Version ${BuildConfig.VERSION_NAME}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Theme Selection Dialog
        if (showThemeDialog) {
            ThemeSelectionDialog(
                currentTheme = selectedTheme,
                onDismiss = { showThemeDialog = false },
                onThemeSelected = { theme ->
                    selectedTheme = theme
                    themePrefs.setTheme(theme)
                    onThemeChange(theme, darkModeEnabled)
                    showThemeDialog = false
                }
            )
        }

        // About Dialog
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("About Tech Savvy") },
                text = {
                    Column {
                        Text("Tech Savvy Solution - Student Learning Platform")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Version: ${BuildConfig.VERSION_NAME}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Developed by: Tech Savvy Solution")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("© 2025 Tech Savvy Solution. All rights reserved.")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: AppTheme,
    onDismiss: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Theme", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    ThemeOption(
                        theme = AppTheme.TECH_SAVVY_BLUE,
                        name = "Tech Savvy Blue",
                        description = "Professional & Trustworthy",
                        primaryColor = Color(0xFF1E40AF),
                        secondaryColor = Color(0xFF7C3AED),
                        isSelected = currentTheme == AppTheme.TECH_SAVVY_BLUE,
                        onClick = { onThemeSelected(AppTheme.TECH_SAVVY_BLUE) }
                    )
                }
                item {
                    ThemeOption(
                        theme = AppTheme.OCEAN_BREEZE,
                        name = "Ocean Breeze",
                        description = "Calm & Refreshing",
                        primaryColor = Color(0xFF0891B2),
                        secondaryColor = Color(0xFF06B6D4),
                        isSelected = currentTheme == AppTheme.OCEAN_BREEZE,
                        onClick = { onThemeSelected(AppTheme.OCEAN_BREEZE) }
                    )
                }
                item {
                    ThemeOption(
                        theme = AppTheme.SUNSET_ORANGE,
                        name = "Sunset Orange",
                        description = "Warm & Energetic",
                        primaryColor = Color(0xFFEA580C),
                        secondaryColor = Color(0xFFF59E0B),
                        isSelected = currentTheme == AppTheme.SUNSET_ORANGE,
                        onClick = { onThemeSelected(AppTheme.SUNSET_ORANGE) }
                    )
                }
                item {
                    ThemeOption(
                        theme = AppTheme.FOREST_GREEN,
                        name = "Forest Green",
                        description = "Natural & Trustworthy",
                        primaryColor = Color(0xFF059669),
                        secondaryColor = Color(0xFF10B981),
                        isSelected = currentTheme == AppTheme.FOREST_GREEN,
                        onClick = { onThemeSelected(AppTheme.FOREST_GREEN) }
                    )
                }
                item {
                    ThemeOption(
                        theme = AppTheme.ROYAL_PURPLE,
                        name = "Royal Purple",
                        description = "Premium & Elegant",
                        primaryColor = Color(0xFF7C3AED),
                        secondaryColor = Color(0xFF9333EA),
                        isSelected = currentTheme == AppTheme.ROYAL_PURPLE,
                        onClick = { onThemeSelected(AppTheme.ROYAL_PURPLE) }
                    )
                }
                item {
                    ThemeOption(
                        theme = AppTheme.DARK_MODE,
                        name = "Pure Dark",
                        description = "AMOLED Friendly",
                        primaryColor = Color(0xFF60A5FA),
                        secondaryColor = Color(0xFF818CF8),
                        isSelected = currentTheme == AppTheme.DARK_MODE,
                        onClick = { onThemeSelected(AppTheme.DARK_MODE) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ThemeOption(
    theme: AppTheme,
    name: String,
    description: String,
    primaryColor: Color,
    secondaryColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, primaryColor) else null
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color Preview
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(secondaryColor)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) primaryColor else TextPrimary
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = primaryColor
                )
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary
        )
    }
}

fun getThemeName(theme: AppTheme): String {
    return when (theme) {
        AppTheme.TECH_SAVVY_BLUE -> "Tech Savvy Blue"
        AppTheme.OCEAN_BREEZE -> "Ocean Breeze"
        AppTheme.SUNSET_ORANGE -> "Sunset Orange"
        AppTheme.FOREST_GREEN -> "Forest Green"
        AppTheme.ROYAL_PURPLE -> "Royal Purple"
        AppTheme.DARK_MODE -> "Pure Dark"
    }
}

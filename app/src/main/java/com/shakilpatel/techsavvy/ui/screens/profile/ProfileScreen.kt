package com.shakilpatel.techsavvy.ui.screens.profile

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.navigation.Screen
import com.shakilpatel.techsavvy.ui.theme.*
import com.shakilpatel.techsavvy.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val currentStudent by authViewModel.currentStudent.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        authViewModel.fetchCurrentStudent()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.EditProfile.route)
                    }) {
                        Icon(Icons.Default.Edit, "Edit Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
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
            // Profile Header
            item {
                ProfileHeader(currentStudent)
            }

            // Personal Information
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Personal Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileInfoRow(Icons.Default.Person, "Name", currentStudent?.name ?: "N/A")
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        ProfileInfoRow(Icons.Default.Email, "Email", currentStudent?.email ?: "N/A")
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        ProfileInfoRow(Icons.Default.Phone, "Phone", currentStudent?.phone ?: "N/A")
                    }
                }
            }

            // Academic Information
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Academic Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileInfoRow(Icons.Default.School, "College", currentStudent?.college ?: "N/A")
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        ProfileInfoRow(Icons.Default.Class, "Year", currentStudent?.year ?: "N/A")
                    }
                }
            }

            // Account Status
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (currentStudent?.status) {
                            "active" -> SecondaryGreen.copy(alpha = 0.1f)
                            "suspended" -> SecondaryRed.copy(alpha = 0.1f)
                            else -> TextTertiary.copy(alpha = 0.1f)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (currentStudent?.status) {
                                "active" -> Icons.Default.CheckCircle
                                "suspended" -> Icons.Default.Block
                                else -> Icons.Default.Info
                            },
                            contentDescription = null,
                            tint = when (currentStudent?.status) {
                                "active" -> SecondaryGreen
                                "suspended" -> SecondaryRed
                                else -> TextSecondary
                            },
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Account Status",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = currentStudent?.status?.replaceFirstChar { it.uppercase() } ?: "Unknown",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (currentStudent?.status) {
                                    "active" -> SecondaryGreen
                                    "suspended" -> SecondaryRed
                                    else -> TextSecondary
                                }
                            )
                        }
                    }
                }
            }

            // Logout Button
            item {
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryRed
                    )
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    Button(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryRed)
                    ) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileHeader(student: com.shakilpatel.techsavvy.data.models.Student?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(GradientStart, GradientEnd)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile Picture
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = student?.name?.first()?.uppercase() ?: "U",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = student?.name ?: "User",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = student?.email ?: "",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}

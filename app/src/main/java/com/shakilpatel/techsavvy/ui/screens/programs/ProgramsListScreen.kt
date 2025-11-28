package com.shakilpatel.techsavvy.ui.screens.programs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.data.models.Enrollment
import com.shakilpatel.techsavvy.data.models.Program
import com.shakilpatel.techsavvy.navigation.Screen
import com.shakilpatel.techsavvy.ui.theme.*
import com.shakilpatel.techsavvy.viewmodel.AuthViewModel
import com.shakilpatel.techsavvy.viewmodel.ProgramViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramsListScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    programViewModel: ProgramViewModel
) {
    val currentStudent by authViewModel.currentStudent.collectAsState()
    val enrolledPrograms by programViewModel.enrolledPrograms.collectAsState()
    val loading by programViewModel.loading.collectAsState()
    LaunchedEffect(true) {
        authViewModel.fetchCurrentStudent()
    }

    LaunchedEffect(currentStudent) {
        currentStudent?.let { student ->
            Log.d("STUDENT IN LIST", student.toString())
            programViewModel.loadEnrolledPrograms(student.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Programs",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Profile.route)
                    }) {
                        Icon(Icons.Default.Person, "Profile")
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.Settings.route)
                    }) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
        ) {
            when {
                loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                enrolledPrograms.isEmpty() -> {
                    EmptyProgramsState()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            WelcomeCard(currentStudent?.name ?: "Student")
                        }

                        items(enrolledPrograms) { (program, enrollment) ->
                            ProgramCard(
                                program = program,
                                enrollment = enrollment,
                                onClick = {
                                    navController.navigate(
                                        Screen.ProgramDashboard.createRoute(program.id)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(studentName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Welcome back, $studentName! 👋",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your enrolled programs are ready to explore",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun ProgramCard(
    program: Program,
    enrollment: Enrollment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = program.status)

                if (enrollment.feeStatus == "pending") {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatusPending.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = StatusPending,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Fee Pending",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = StatusPending
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Program Name
            Text(
                text = program.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = program.description,
                fontSize = 14.sp,
                color = TextSecondary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(
                    icon = Icons.Default.Category,
                    text = program.category.capitalize()
                )
                InfoChip(
                    icon = Icons.Default.DateRange,
                    text = "${program.startDate} - ${program.endDate}"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fee Info
            if (enrollment.feeStatus == "pending") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = SecondaryOrange.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            tint = SecondaryOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pending Fee Payment",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            Text(
                                text = "₹${enrollment.feeAmount}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecondaryOrange
                            )
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = StatusPaid.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = StatusPaid,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fee Paid - ₹${enrollment.feeAmount}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = StatusPaid
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // View Button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Dashboard")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (color, icon) = when (status.lowercase()) {
        "active" -> StatusActive to Icons.Default.PlayArrow
        "upcoming" -> PrimaryBlue to Icons.Default.Schedule
        "completed" -> StatusPaid to Icons.Default.CheckCircle
        else -> TextSecondary to Icons.Default.Info
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.capitalize(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun EmptyProgramsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = TextTertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Programs Yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You're not enrolled in any programs at the moment",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

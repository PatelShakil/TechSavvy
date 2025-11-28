package com.shakilpatel.techsavvy.ui.screens.programs.tabs

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shakilpatel.techsavvy.data.models.Session
import com.shakilpatel.techsavvy.ui.theme.*
import com.shakilpatel.techsavvy.viewmodel.ProgramViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SessionsTab(
    programId: String,
    programViewModel: ProgramViewModel
) {
    val sessions by programViewModel.sessions.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(programId) {
        programViewModel.loadSessions(programId)
    }

    if (sessions.isEmpty()) {
        EmptySessionsState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sessions) { session ->
                SessionCard(
                    session = session,
                    onJoinClick = {
                        if (session.meetingLink.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(session.meetingLink))
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SessionCard(session: Session, onJoinClick: () -> Unit) {
    val isUpcoming = isSessionUpcoming(session.dateTime)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUpcoming) PrimaryBlue.copy(alpha = 0.05f) else Color.White
        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        border = CardDefaults.outlinedCardBorder(
            true
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = if (isUpcoming) PrimaryBlue else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = session.sessionName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                if (isUpcoming) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SecondaryGreen.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Upcoming",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SecondaryGreen,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TextTertiary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Completed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = session.description,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    icon = Icons.Default.CalendarToday,
                    text = formatDate(session.dateTime)
                )
                InfoItem(
                    icon = Icons.Default.AccessTime,
                    text = formatTime(session.dateTime)
                )
            }

            // Tags
            if (session.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    session.tags.take(3).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SecondaryPurple.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                color = SecondaryPurple,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Join Button
            if (session.meetingLink.isNotEmpty() && isUpcoming) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onJoinClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoCall,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Join Session", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun EmptySessionsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.EventNote,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = TextTertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Sessions Yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sessions will be added soon",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

fun isSessionUpcoming(dateTime: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val sessionDate = sdf.parse(dateTime)
        sessionDate?.after(Date()) ?: false
    } catch (e: Exception) {
        false
    }
}

fun formatDate(dateTime: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateTime
    }
}

fun formatTime(dateTime: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateTime
    }
}

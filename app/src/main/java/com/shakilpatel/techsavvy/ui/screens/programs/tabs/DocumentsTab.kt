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
import com.shakilpatel.techsavvy.data.models.Document
import com.shakilpatel.techsavvy.ui.theme.*
import com.shakilpatel.techsavvy.viewmodel.ProgramViewModel
import kotlinx.coroutines.launch

@Composable
fun DocumentsTab(
    programId: String,
    studentId: String,
    programViewModel: ProgramViewModel
) {
    var documents by remember { mutableStateOf<List<Document>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(programId, studentId) {
        scope.launch {
            val result = programViewModel.getDocuments(programId, studentId)
            result.onSuccess { docs ->
                documents = docs
                loading = false
            }.onFailure {
                loading = false
            }
        }
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (documents.isEmpty()) {
        EmptyDocumentsState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                InfoCard()
            }

            // Group by type
            val offerLetters = documents.filter { it.type == "offer_letter" }
            val certificates = documents.filter { it.type == "certificate" }

            if (offerLetters.isNotEmpty()) {
                item {
                    Text(
                        text = "📄 Offer Letters",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(offerLetters) { document ->
                    DocumentCard(
                        document = document,
                        onDownload = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(document.downloadUrl))
                            context.startActivity(intent)
                        }
                    )
                }
            }

            if (certificates.isNotEmpty()) {
                item {
                    Text(
                        text = "🏆 Certificates",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }
                items(certificates) { document ->
                    DocumentCard(
                        document = document,
                        onDownload = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(document.downloadUrl))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryBlue.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Your Documents",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Offer letters and certificates will appear here",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun DocumentCard(document: Document, onDownload: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when (document.type) {
                    "offer_letter" -> SecondaryOrange.copy(alpha = 0.1f)
                    "certificate" -> SecondaryGreen.copy(alpha = 0.1f)
                    else -> TextTertiary.copy(alpha = 0.1f)
                },
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = when (document.type) {
                            "offer_letter" -> Icons.Default.Description
                            "certificate" -> Icons.Default.EmojiEvents
                            else -> Icons.Default.InsertDriveFile
                        },
                        contentDescription = null,
                        tint = when (document.type) {
                            "offer_letter" -> SecondaryOrange
                            "certificate" -> SecondaryGreen
                            else -> TextSecondary
                        },
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Document Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = document.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = document.type.replace("_", " ").split(" ").joinToString(" ") {
                        it.replaceFirstChar { char -> char.uppercase() }
                    },
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                if (document.createdAt != null) {
                    Text(
                        text = formatDocumentDate(document.createdAt.toDate()),
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }
            }

            // Download Button
            IconButton(
                onClick = onDownload,
                modifier = Modifier
                    .size(48.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            tint = PrimaryBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyDocumentsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = TextTertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Documents Yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your certificates and letters will appear here",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

fun formatDocumentDate(date: java.util.Date): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(date)
}

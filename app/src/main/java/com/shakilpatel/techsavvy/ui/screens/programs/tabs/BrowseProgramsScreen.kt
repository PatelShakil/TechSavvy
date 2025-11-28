package com.shakilpatel.techsavvy.ui.screens.programs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shakilpatel.techsavvy.data.models.Program
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseProgramsScreen(navController: NavController) {
    var programs by remember { mutableStateOf<List<Program>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf("all") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val snapshot = firestore.collection("programs")
                .whereIn("status", listOf("active", "upcoming"))
                .orderBy("startDate", Query.Direction.DESCENDING)
                .get()
                .await()

            programs = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Program::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            loading = false
        }
    }

    val filteredPrograms = if (selectedFilter == "all") {
        programs
    } else {
        programs.filter { it.category == selectedFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Browse Programs") },
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
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header Banner
            item {
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
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Explore Our Programs",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Join Tech Savvy Solution and transform your career",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Filters
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        label = "All",
                        selected = selectedFilter == "all",
                        onClick = { selectedFilter = "all" }
                    )
                    FilterChip(
                        label = "Internship",
                        selected = selectedFilter == "internship",
                        onClick = { selectedFilter = "internship" }
                    )
                    FilterChip(
                        label = "Mentorship",
                        selected = selectedFilter == "mentorship",
                        onClick = { selectedFilter = "mentorship" }
                    )
                    FilterChip(
                        label = "Workshop",
                        selected = selectedFilter == "workshop",
                        onClick = { selectedFilter = "workshop" }
                    )
                }
            }

            // Loading
            if (loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Empty State
            if (!loading && filteredPrograms.isEmpty()) {
                item {
                    EmptyProgramsListState()
                }
            }

            // Programs List
            items(filteredPrograms) { program ->
                PublicProgramCard(
                    program = program,
                    onApply = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
                            "https://techsavvysolution.patelshakil.tech/programs/apply/${program.id}"
                        ))
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // CTA Footer
            item {
                Spacer(modifier = Modifier.height(8.dp))
                CTAFooter(
                    onVisitWebsite = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
                            "https://techsavvysolution.patelshakil.tech"
                        ))
                        context.startActivity(intent)
                    },
                    onContactUs = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
                            "https://techsavvysolution.patelshakil.tech/contactus"
                        ))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun PublicProgramCard(program: Program, onApply: () -> Unit) {
    val deadlinePassed = isDeadlinePassed(program.applicationDeadline ?: Date().toString())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header with gradient
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
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = program.category.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (program.status == "active")
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                        else
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = if (program.status == "active") "Active" else "Upcoming",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Content
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = program.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = program.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Details
                ProgramDetailRow(
                    Icons.Default.CalendarToday,
                    "${program.startDate} to ${program.endDate}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgramDetailRow(
                    Icons.Default.AccessTime,
                    "Apply by: ${program.applicationDeadline}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgramDetailRow(
                    Icons.Default.Group,
                    "Group: ${program.minGroupSize}-${program.maxGroupSize} students"
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgramDetailRow(
                    Icons.Default.CurrencyRupee,
                    "₹${program.feePerStudent} per student",
                    bold = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Features
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        FeatureCheckItem("✓ Live project deployment")
                        FeatureCheckItem("✓ Certificate on completion")
                        FeatureCheckItem("✓ Mentorship & support")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Apply Button
                if (deadlinePassed) {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Applications Closed")
                    }
                } else {
                    Button(
                        onClick = onApply,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Apply Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramDetailRow(icon: ImageVector, text: String, bold: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = if (bold)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun FeatureCheckItem(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
fun EmptyProgramsListState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Programs Available",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Check back soon for new opportunities!",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CTAFooter(onVisitWebsite: () -> Unit, onContactUs: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Ready to Start Your Journey?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Join hundreds of students who transformed their careers",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onVisitWebsite,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("Visit Website")
                    }
                    Button(
                        onClick = onContactUs,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Contact Us")
                    }
                }
            }
        }
    }
}

fun isDeadlinePassed(deadline: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val deadlineDate = sdf.parse(deadline)
        deadlineDate?.before(Date()) ?: false
    } catch (e: Exception) {
        false
    }
}

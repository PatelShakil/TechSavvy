package com.shakilpatel.techsavvy.ui.screens.programs.tabs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shakilpatel.techsavvy.data.models.Milestone
import com.shakilpatel.techsavvy.data.models.Program
import com.shakilpatel.techsavvy.ui.theme.*

@Composable
fun MilestonesTab(program: Program) {
    if (program.milestones.isEmpty()) {
        EmptyMilestonesState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            itemsIndexed(program.milestones) { index, milestone ->
                MilestoneItem(
                    milestone = milestone,
                    isFirst = index == 0,
                    isLast = index == program.milestones.size - 1,
                    index = index + 1
                )
            }
        }
    }
}

@Composable
fun MilestoneItem(
    milestone: Milestone,
    isFirst: Boolean,
    isLast: Boolean,
    index: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        val primary = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

        // Timeline indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {

            // Top line
            if (!isFirst) {
                Canvas(
                    modifier = Modifier
                        .width(2.dp)
                        .height(20.dp)
                ) {
                    drawLine(
                        color = primary,
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Circle with number
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = index.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bottom line
            if (!isLast) {
                Canvas(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                ) {
                    drawLine(
                        color = primary,
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Milestone card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (!isLast) 0.dp else 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = milestone.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                if (milestone.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = milestone.description,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = SecondaryOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Due: ${milestone.dueDate}",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMilestonesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = TextTertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Milestones Set",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Milestones will be added by your mentor",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

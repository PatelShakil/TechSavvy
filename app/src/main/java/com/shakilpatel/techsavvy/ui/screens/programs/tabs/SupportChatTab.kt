package com.shakilpatel.techsavvy.ui.screens.programs.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shakilpatel.techsavvy.data.models.Student
import com.shakilpatel.techsavvy.data.models.SupportTicket
import com.shakilpatel.techsavvy.data.models.TicketMessage
import com.shakilpatel.techsavvy.ui.theme.*
import com.shakilpatel.techsavvy.viewmodel.ProgramViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SupportChatTab(
    programId: String,
    student: Student?,
    programViewModel: ProgramViewModel
) {
    // Get ALL tickets for this student
    val tickets by programViewModel.getSupportTickets(programId, student?.id ?: "")
        .collectAsState(initial = emptyList())

    var selectedTicketId by remember { mutableStateOf<String?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Auto-select first ticket ONLY ONCE when tickets first load
    LaunchedEffect(tickets.isEmpty()) {
        if (tickets.isNotEmpty() && selectedTicketId == null) {
            selectedTicketId = tickets.first().id
        }
        isLoading = false
    }

    // Find selected ticket
    val selectedTicket = tickets.find { it.id == selectedTicketId }

    // Show loading only on initial load
    if (isLoading && tickets.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Main Layout - Always show this structure to prevent layout shift
    Row(modifier = Modifier.fillMaxSize()) {
        // Left Panel - Ticket List (35% width)
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .fillMaxHeight(),
            color = BackgroundLight,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Support Tickets",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${tickets.size} ticket${if (tickets.size != 1) "s" else ""}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Create New Ticket Button
                Button(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New", fontSize = 14.sp)
                }

                // Tickets List
                if (tickets.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = TextTertiary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No tickets yet",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tickets, key = { it.id }) { ticket ->
                            TicketListItem(
                                ticket = ticket,
                                isSelected = selectedTicketId == ticket.id,
                                onClick = { selectedTicketId = ticket.id }
                            )
                        }
                    }
                }
            }
        }

        // Right Panel - Ticket Chat (65% width)
        if (selectedTicket != null) {
            SupportChatView(
                ticket = selectedTicket,
                student = student,
                programViewModel = programViewModel,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            // Empty state when no ticket selected
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(BackgroundLight),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = TextTertiary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (tickets.isEmpty()) "Create your first ticket" else "Select a ticket to view",
                        fontSize = 16.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }

    // Create Ticket Dialog
    if (showCreateDialog) {
        CreateTicketDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { subject, message ->
                if (student != null) {
                    programViewModel.createSupportTicket(
                        programId = programId,
                        studentId = student.id,
                        studentName = student.name,
                        subject = subject,
                        message = message
                    )
                }
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun TicketListItem(
    ticket: SupportTicket,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header with status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.subject,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (ticket.status == "open")
                        SecondaryGreen.copy(alpha = 0.2f)
                    else
                        TextTertiary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = ticket.status.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (ticket.status == "open") SecondaryGreen else TextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Last message preview
            if (ticket.messages.isNotEmpty()) {
                val lastMessage = ticket.messages.last()
                Text(
                    text = lastMessage.message,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${ticket.messages.size}",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }

                Text(
                    text = formatTicketDate(ticket.updatedAt?.toDate() ?: ticket.createdAt?.toDate()),
                    fontSize = 11.sp,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
fun CreateTicketDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String) -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Support Ticket") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (subject.isNotBlank() && message.isNotBlank()) {
                        onCreate(subject, message)
                    }
                },
                enabled = subject.isNotBlank() && message.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SupportChatView(
    ticket: SupportTicket,
    student: Student?,
    programViewModel: ProgramViewModel,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when messages change
    LaunchedEffect(ticket.messages.size) {
        if (ticket.messages.isNotEmpty()) {
            listState.animateScrollToItem(ticket.messages.size - 1)
        }
    }

    Surface(
        modifier = modifier.fillMaxHeight(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (ticket.status == "open")
                    SecondaryGreen.copy(alpha = 0.1f)
                else
                    TextTertiary.copy(alpha = 0.1f),
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ticket.subject,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Ticket #${ticket.id.take(8)}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (ticket.status == "open")
                                SecondaryGreen.copy(alpha = 0.2f)
                            else
                                TextTertiary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = ticket.status.uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (ticket.status == "open") SecondaryGreen else TextSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(BackgroundLight),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ticket.messages.size) { index ->
                    val message = ticket.messages[index]
                    SupportMessageBubble(
                        message = message,
                        isOwnMessage = message.senderType == "student"
                    )
                }
            }

            // Input (only if ticket is open)
            if (ticket.status == "open") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Reply to admin...") },
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = BorderLight,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank() && student != null) {
                                    programViewModel.sendSupportMessage(
                                        ticketId = ticket.id,
                                        studentName = student.name,
                                        message = messageText,
                                        currentMessages = ticket.messages
                                    )
                                    messageText = ""
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = if (messageText.isNotBlank()) MaterialTheme.colorScheme.primary else TextTertiary,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White
                            )
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = TextTertiary.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "This ticket has been resolved",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary
                            )
                            Text(
                                text = "You can create a new ticket for other issues",
                                fontSize = 12.sp,
                                color = TextTertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SupportMessageBubble(message: TicketMessage, isOwnMessage: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start
        ) {
            if (!isOwnMessage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = SecondaryGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Admin Support",
                        fontSize = 11.sp,
                        color = SecondaryGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isOwnMessage) 16.dp else 4.dp,
                    bottomEnd = if (isOwnMessage) 4.dp else 16.dp
                ),
                color = if (isOwnMessage) MaterialTheme.colorScheme.primary else Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.message,
                        fontSize = 14.sp,
                        color = if (isOwnMessage) Color.White else TextPrimary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatMessageTime(message.timestamp?.toDate()),
                        fontSize = 10.sp,
                        color = if (isOwnMessage) Color.White.copy(alpha = 0.7f) else TextTertiary
                    )
                }
            }
        }
    }
}

fun formatTicketDate(date: Date?): String {
    if (date == null) return "Just now"
    val now = Date()
    val diff = now.time - date.time

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
    }
}

//fun formatMessageTime(date: Date?): String {
//    if (date == null) return "Now"
//    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
//    return sdf.format(date)
//}

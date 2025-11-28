package com.shakilpatel.techsavvy.ui.screens.programs.tabs

import androidx.compose.foundation.background
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
import com.shakilpatel.techsavvy.data.models.ChatMessage
import com.shakilpatel.techsavvy.data.models.Student
import com.shakilpatel.techsavvy.ui.theme.*
import com.shakilpatel.techsavvy.viewmodel.ProgramViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GroupChatTab(
    programId: String,
    student: Student?,
    programViewModel: ProgramViewModel
) {
    val messages by programViewModel.getGroupChatMessages(programId).collectAsState(initial = emptyList())
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Chat Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Group Chat",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${messages.size} messages",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Pinned Messages
        val pinnedMessages = messages.filter { it.isPinned }
        if (pinnedMessages.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = StatusPending.copy(alpha = 0.1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = null,
                            tint = StatusPending,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pinned Message",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusPending
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    pinnedMessages.forEach { msg ->
                        Text(
                            text = msg.message,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // Messages List
        if (messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextTertiary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No messages yet",
                        fontSize = 16.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Start the conversation!",
                        fontSize = 14.sp,
                        color = TextTertiary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(BackgroundLight),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    ChatMessageBubble(
                        message = message,
                        isOwnMessage = message.senderId == student?.id
                    )
                }
            }
        }

        // Input Field
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
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = BorderLight
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank() && student != null) {
                            programViewModel.sendGroupMessage(
                                programId = programId,
                                studentId = student.id,
                                studentName = student.name,
                                message = messageText
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
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage, isOwnMessage: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start
        ) {
            // Sender name (if not own message)
            if (!isOwnMessage) {
                Text(
                    text = "${message.senderName}${if (message.senderType == "admin") " (Admin)" else ""}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }

            // Message bubble
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

fun formatMessageTime(date: Date?): String {
    if (date == null) return "Now"
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(date)
}

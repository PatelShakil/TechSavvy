package com.shakilpatel.techsavvy.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Program(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val status: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val feePerStudent: Long = 0,
    val milestones: List<Milestone> = emptyList(),
    val createdAt: Timestamp? = null,
    val applicationDeadline: String? = null,
    val minGroupSize: Int = 0,
    val maxGroupSize: Int = 0,
)

data class Milestone(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val order: Int = 0
)

data class Enrollment(
    val id: String = "",
    val programId: String = "",
    val studentId: String = "",
    val groupId: String = "",
    val feeStatus: String = "pending", // paid/pending
    val feeAmount: Long = 0,
    val joinedAt: Timestamp? = null
)

data class Group(
    val id: String = "",
    val programId: String = "",
    val groupName: String = "",
    val members: List<String> = emptyList(),
    val projectTitle: String = "",
    val totalFee: Long = 0,
    val createdAt: Timestamp? = null
)

data class Student(
    val id: String = "",
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val college: String = "",
    val year: String = "",
    val profileImage: String? = null,
    val status: String = "active", // active/suspended/pending
    val createdAt: Timestamp? = null
)

data class Session(
    val id: String = "",
    val programId: String = "",
    val sessionName: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val meetingLink: String = "",
    val dateTime: String = "",
    val attendance: List<String> = emptyList(),
    val createdAt: Timestamp? = null
)

data class ChatMessage(
    var id: String = "",
    var programId: String = "",
    var senderId: String = "",
    var senderName: String = "",
    var senderType: String = "", // admin/student
    var message: String = "",
    var timestamp: Timestamp? = null,
    @get:PropertyName("isHidden") @set:PropertyName("isHidden")
    var isHidden: Boolean = false,
    @get:PropertyName("isPinned") @set:PropertyName("isPinned")
    var isPinned: Boolean = false
)


data class SupportTicket(
    val id: String = "",
    val programId: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val subject: String = "",
    val status: String = "open", // open/resolved
    val messages: List<TicketMessage> = emptyList(),
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

data class TicketMessage(
    val sender: String = "",
    val senderType: String = "",
    val message: String = "",
    val timestamp: Timestamp? = null
)

data class Document(
    val id: String = "",
    val programId: String = "",
    val studentId: String = "",
    val type: String = "", // offer_letter/certificate
    val title: String = "",
    val downloadUrl: String = "",
    val createdAt: Timestamp? = null
)
data class Recording(
    val id: String = "",
    val programId: String = "",
    val sessionId: String? = null,
    val title: String = "",
    val description: String = "",
    val youtubeUrl: String = "",
    val thumbnailUrl: String? = null,
    val duration: String? = null,
    val tags: List<String> = emptyList(),
    val uploadedAt: Timestamp? = null,
    val uploadedBy: String = "",
    val views: Int = 0
)

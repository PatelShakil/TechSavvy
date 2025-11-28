package com.shakilpatel.techsavvy.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shakilpatel.techsavvy.data.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProgramRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getEnrolledPrograms(studentId: String): Result<List<Pair<Program, Enrollment>>> {
        return try {
            Log.d("STUDENT ID",studentId)
            // Get enrollments
            val enrollments = firestore.collection("programEnrollments")
                .whereEqualTo("studentId", studentId)
//                .whereEqualTo("studentId", "ctk14s0ApJZu4wPTdz4M7CFaYsu2")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Enrollment::class.java)?.copy(id = it.id) }

            Log.d("Enrollments",enrollments.toString())

            // Get programs
            val programsList = mutableListOf<Pair<Program, Enrollment>>()
            for (enrollment in enrollments) {
                val program = firestore.collection("programs")
                    .document(enrollment.programId)
                    .get()
                    .await()
                    .toObject(Program::class.java)
                    ?.copy(id = enrollment.programId)

                if (program != null) {
                    programsList.add(Pair(program, enrollment))
                }
            }

            Result.success(programsList)
        } catch (e: Exception) {
            Log.d("Enrollments",e.message.toString())

            Result.failure(e)
        }
    }

    suspend fun getProgramById(programId: String): Result<Program> {
        return try {
            val program = firestore.collection("programs")
                .document(programId)
                .get()
                .await()
                .toObject(Program::class.java)
                ?.copy(id = programId)
                ?: return Result.failure(Exception("Program not found"))

            Result.success(program)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroup(programId: String, studentId: String): Result<Group> {
        return try {
            Log.d("STUDENT ID",studentId)
            Log.d("PROGRAM ID",programId)

            val querySnapshot = firestore.collection("groups")
                .whereEqualTo("programId", programId)
                .whereArrayContains("members", studentId)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return Result.failure(Exception("Group not found"))
            }

            val doc = querySnapshot.documents.first()
            val group = doc.toObject(Group::class.java)?.copy(id = doc.id)
                ?: return Result.failure(Exception("Failed to parse group"))

            Result.success(group)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getSessions(programId: String): Result<List<Session>> {
        return try {
            val sessions = firestore.collection("sessions")
                .whereEqualTo("programId", programId)
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Session::class.java)?.copy(id = it.id) }

            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getGroupChatMessages(programId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = firestore.collection("groupChat")
            .whereEqualTo("programId", programId)
            .whereEqualTo("isHidden", false)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull {
                    Log.d("CHAT OBJ",it.toString());
                    it.toObject(ChatMessage::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(messages)
            }

        awaitClose { listener.remove() }
    }

    suspend fun sendGroupMessage(
        programId: String,
        studentId: String,
        studentName: String,
        message: String
    ): Result<Unit> {
        return try {
            firestore.collection("groupChat").add(
                hashMapOf(
                    "programId" to programId,
                    "senderId" to studentId,
                    "senderName" to studentName,
                    "senderType" to "student",
                    "message" to message,
                    "timestamp" to com.google.firebase.Timestamp.now(),
                    "isHidden" to false,
                    "isPinned" to false
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSupportTickets(programId: String, studentId: String): Flow<List<SupportTicket>> = callbackFlow {
        val listener = firestore.collection("supportTickets")
            .whereEqualTo("programId", programId)
            .whereEqualTo("studentId", studentId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val tickets = snapshot?.documents?.mapNotNull {
                    it.toObject(SupportTicket::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(tickets)
            }

        awaitClose { listener.remove() }
    }

    suspend fun createSupportTicket(
        programId: String,
        studentId: String,
        studentName: String,
        subject: String,
        message: String
    ): Result<Unit> {
        return try {
            firestore.collection("supportTickets").add(
                hashMapOf(
                    "programId" to programId,
                    "studentId" to studentId,
                    "studentName" to studentName,
                    "subject" to subject,
                    "status" to "open",
                    "messages" to listOf(
                        hashMapOf(
                            "sender" to studentName,
                            "senderType" to "student",
                            "message" to message,
                            "timestamp" to com.google.firebase.Timestamp.now()
                        )
                    ),
                    "createdAt" to com.google.firebase.Timestamp.now(),
                    "updatedAt" to com.google.firebase.Timestamp.now()
                )
            ).await()
            Log.d("SUPPORT TICKET", "CREATED")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("SUPPORT TICKET", e.message.toString())

            Result.failure(e)
        }
    }

    suspend fun sendSupportMessage(
        ticketId: String,
        studentName: String,
        message: String,
        currentMessages: List<TicketMessage>
    ): Result<Unit> {
        return try {
            val newMessage = TicketMessage(
                sender = studentName,
                senderType = "student",
                message = message,
                timestamp = com.google.firebase.Timestamp.now()
            )

            firestore.collection("supportTickets")
                .document(ticketId)
                .update(
                    mapOf(
                        "messages" to currentMessages + newMessage,
                        "updatedAt" to com.google.firebase.Timestamp.now()
                    )
                ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDocuments(programId: String, studentId: String): Result<List<Document>> {
        return try {
            val documents = firestore.collection("documents")
                .whereEqualTo("programId", programId)
                .whereEqualTo("studentId", studentId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Document::class.java)?.copy(id = it.id) }

            Result.success(documents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

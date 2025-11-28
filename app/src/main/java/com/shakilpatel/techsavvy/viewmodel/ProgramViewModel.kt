package com.shakilpatel.techsavvy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakilpatel.techsavvy.data.models.*
import com.shakilpatel.techsavvy.data.repository.ProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel  @Inject constructor()  : ViewModel() {
    private val repository = ProgramRepository()

    private val _enrolledPrograms = MutableStateFlow<List<Pair<Program, Enrollment>>>(emptyList())
    val enrolledPrograms: StateFlow<List<Pair<Program, Enrollment>>> = _enrolledPrograms.asStateFlow()

    private val _currentProgram = MutableStateFlow<Program?>(null)
    val currentProgram: StateFlow<Program?> = _currentProgram.asStateFlow()

    private val _currentGroup = MutableStateFlow<Group?>(null)
    val currentGroup: StateFlow<Group?> = _currentGroup.asStateFlow()

    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions: StateFlow<List<Session>> = _sessions.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadEnrolledPrograms(studentId: String) {
        viewModelScope.launch {
            _loading.value = true
            Log.d("STUDENT DOC",studentId.toString())

            val result = repository.getEnrolledPrograms(studentId)
            result.onSuccess { programs ->
                _enrolledPrograms.value = programs
                _loading.value = false
            }.onFailure { exception ->
                _error.value = exception.message
                _loading.value = false
            }
        }
    }

    fun loadProgram(programId: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getProgramById(programId)
            result.onSuccess { program ->
                _currentProgram.value = program
                _loading.value = false
            }.onFailure { exception ->
                _error.value = exception.message
                _loading.value = false
            }
        }
    }

    fun loadGroup(programId: String,studentId: String) {
        viewModelScope.launch {
            val result = repository.getGroup(programId,studentId)
            result.onSuccess { group ->
                _currentGroup.value = group
            }
        }
    }

    fun loadSessions(programId: String) {
        viewModelScope.launch {
            val result = repository.getSessions(programId)
            result.onSuccess { sessions ->
                _sessions.value = sessions
            }
        }
    }

    fun getGroupChatMessages(programId: String): Flow<List<ChatMessage>> {
        return repository.getGroupChatMessages(programId)
    }

    fun sendGroupMessage(
        programId: String,
        studentId: String,
        studentName: String,
        message: String
    ) {
        viewModelScope.launch {
            repository.sendGroupMessage(programId, studentId, studentName, message)
        }
    }

    fun getSupportTickets(programId: String, studentId: String): Flow<List<SupportTicket>> {
        return repository.getSupportTickets(programId, studentId)
    }
    fun createSupportTicket(
        programId: String,
        studentId: String,
        studentName: String,
        subject: String,
        message: String
    ) {
        viewModelScope.launch {
            repository.createSupportTicket(programId, studentId, studentName, subject, message)
        }
    }

    fun sendSupportMessage(
        ticketId: String,
        studentName: String,
        message: String,
        currentMessages: List<TicketMessage>
    ) {
        viewModelScope.launch {
            repository.sendSupportMessage(ticketId, studentName, message, currentMessages)
        }
    }

    suspend fun getDocuments(programId: String, studentId: String): Result<List<Document>> {
        return repository.getDocuments(programId, studentId)
    }
}

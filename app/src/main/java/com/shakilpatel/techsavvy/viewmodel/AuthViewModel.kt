package com.shakilpatel.techsavvy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakilpatel.techsavvy.data.models.Student
import com.shakilpatel.techsavvy.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _currentStudent = MutableStateFlow<Student?>(null)
    val currentStudent: StateFlow<Student?> = _currentStudent.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = repository.login(email, password)

            result.onSuccess { student ->
                _currentStudent.value = student
                _loginState.value = LoginState.Success(student)
            }.onFailure { exception ->
                _loginState.value = LoginState.Error(exception.message ?: "Login failed")
            }
        }
    }

    fun fetchCurrentStudent(){
        viewModelScope.launch {
            val result = repository.fetchCurrentStudent()
            result.onSuccess { student ->
                _currentStudent.value = student
                _loginState.value = LoginState.Success(student)
            }.onFailure { exception ->
//                _loginState.value = LoginState.Error(exception.message ?: "Login failed")
            }
        }
    }
    fun logout() {
        repository.logout()
        _currentStudent.value = null
        _loginState.value = LoginState.Idle
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun isLoggedIn(): Boolean = repository.isLoggedIn()
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val student: Student) : LoginState()
    data class Error(val message: String) : LoginState()
}

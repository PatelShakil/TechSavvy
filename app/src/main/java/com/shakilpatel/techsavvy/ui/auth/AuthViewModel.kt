package com.shakilpatel.techsavvy.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.techsavvy.data.models.StudentModel
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.repo.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val db: FirebaseFirestore,
    private val repository: StudentRepository
) : ViewModel() {

    private val _student = MutableStateFlow<Resource<StudentModel>?>(null)
    val student = _student

    init {
        getStudent()
    }

    fun saveStudent(student: StudentModel) {
        repository.saveStudent(student)
    }

    fun getStudent() {
        _student.value = Resource.Loading
        _student.value = repository.getStudent()
    }


    private val _loginResult = MutableStateFlow<Resource<Boolean>?>(null)
    val loginResult = _loginResult

    fun login(phoneNo: String, otpValue: String) {
        _loginResult.value = Resource.Loading
        db.collection("students")
            .whereEqualTo("phone", phoneNo)
            .whereEqualTo("pin", otpValue)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _loginResult.value = Resource.Failure(error.message ?: "")
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    Log.d("STD", value.documents.toString())
                    val std = value.documents[0].toObject(StudentModel::class.java)
                    Log.d("STD", std.toString())
                    if (std != null && std.issub) {
                        saveStudent(std)
                        _loginResult.value = Resource.Success(true)
                    } else {
                        _loginResult.value = Resource.Failure("You are not subscribed")
                    }
                }
            }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun checkStudent(result: StudentModel) {
        login(result.phone,result.pin)
    }
}
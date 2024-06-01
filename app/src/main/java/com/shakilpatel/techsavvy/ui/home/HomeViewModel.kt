package com.shakilpatel.techsavvy.ui.home

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.techsavvy.data.models.StudentModel
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.repo.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val repository: StudentRepository
) : ViewModel() {


    private val _studentDetails = MutableStateFlow<Resource<StudentModel>?>(null)
    val studentDetails = _studentDetails

    private val _studentLocal = MutableStateFlow<Resource<StudentModel>?>(null)
    val studentLocal = _studentLocal


    init {
        getStudentLocal()
    }

    fun getStudentLocal() {
        _studentLocal.value = Resource.Loading
        _studentLocal.value = repository.getStudent()
    }


    fun getStudentDetails(phone: String) {
        db.collection("students")
            .whereEqualTo("phone", phone)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _studentDetails.value = Resource.Failure(error.message ?: "")
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    val std = value.documents[0].toObject(StudentModel::class.java)
                    _studentDetails.value = Resource.Success(std!!)
                }
            }
    }


}
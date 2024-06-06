package com.shakilpatel.techsavvy.ui.students

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.techsavvy.data.models.StudentModel
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.repo.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val db : FirebaseFirestore,
    private val std : StudentRepository
): ViewModel() {


    private val _students = MutableStateFlow<Resource<List<StudentModel>>?>(null)
    val students = _students

    init {
        getStudents(std.getStudentByKey("gender")?:"")
    }

    fun getStudents(gender :String) = viewModelScope.launch {
        _students.value = Resource.Loading
        db.collection("students")
            .whereEqualTo("gender", gender)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _students.value = Resource.Failure(error.message.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val students = value.toObjects(StudentModel::class.java)
                    _students.value = Resource.Success(students)
                } else {
                    _students.value = Resource.Failure("Something went wrong")
                }
            }
    }

}
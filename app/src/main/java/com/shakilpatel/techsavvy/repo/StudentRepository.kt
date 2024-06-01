package com.shakilpatel.techsavvy.repo

import android.content.SharedPreferences
import com.shakilpatel.techsavvy.data.models.StudentModel
import com.shakilpatel.techsavvy.data.resources.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) {

    fun saveStudent(student: StudentModel) {
        editor.putString("uid", student.uid)
        editor.putString("name", student.name)
        editor.putString("phone", student.phone)
        editor.putString("email", student.email)
        editor.putString("pin", student.pin)
        editor.putString("gender", student.gender)
        editor.putBoolean("issub", student.issub)
        editor.apply()
    }

    fun getStudent(): Resource<StudentModel> {

        if(sharedPreferences.getString("phone",null) != null){
            return Resource.Success(StudentModel(
                sharedPreferences.getString("uid", "") ?: "",
                sharedPreferences.getString("name", "") ?: "",
                sharedPreferences.getString("phone", "") ?: "",
                sharedPreferences.getString("email", "") ?: "",
                sharedPreferences.getString("pin", "") ?: "",
                sharedPreferences.getString("gender", "") ?: "",
                sharedPreferences.getBoolean("issub", false)
            ))
        }else{
            return Resource.Failure("No student found")
        }



    }

    fun updateStudent(student: StudentModel) {
        saveStudent(student)
    }

    fun deleteStudent() {
        editor.clear()
        editor.apply()
    }
}

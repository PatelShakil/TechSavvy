package com.shakilpatel.techsavvy.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.techsavvy.data.models.Student
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<Student> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("User not found"))



            // Fetch student data
            val studentDoc = firestore.collection("students")
                .whereEqualTo("uid", uid)
                .get()
                .await()

            Log.d("STUDENT DOC",studentDoc.toString())

            if (studentDoc.isEmpty) {
                return Result.failure(Exception("Student profile not found"))
            }

            val student = studentDoc.documents[0].toObject(Student::class.java)
                ?.copy(id = studentDoc.documents[0].id)
                ?: return Result.failure(Exception("Failed to parse student data"))

            // Check if student is suspended
            if (student.status == "suspended") {
                auth.signOut()
                return Result.failure(Exception("Your account has been suspended. Please contact admin."))
            }

            Result.success(student)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun fetchCurrentStudent(): Result<Student> {
        return try {
            val uid = getCurrentUserId() ?: return Result.failure(Exception("User not found"))



            // Fetch student data
            val studentDoc = firestore.collection("students")
                .whereEqualTo("uid", uid)
                .get()
                .await()

            Log.d("STUDENT FETCH",studentDoc.toString())

            if (studentDoc.isEmpty) {
                return Result.failure(Exception("Student profile not found"))
            }

            val student = studentDoc.documents[0].toObject(Student::class.java)
                ?.copy(id = studentDoc.documents[0].id)
                ?: return Result.failure(Exception("Failed to parse student data"))

            // Check if student is suspended
            if (student.status == "suspended") {
                auth.signOut()
                return Result.failure(Exception("Your account has been suspended. Please contact admin."))
            }

            Result.success(student)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun isLoggedIn(): Boolean = auth.currentUser != null
}

package com.shakilpatel.techsavvy.ui.discussion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.techsavvy.data.models.MessageGDModel
import com.shakilpatel.techsavvy.data.models.StudentModel
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.repo.StudentRepository
import com.shakilpatel.techsavvy.utils.generateRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class DiscussionViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val std: StudentRepository
) : ViewModel() {

    private val _gdMsg = MutableStateFlow<Resource<List<MessageGDModel>>?>(null)
    val gdMsg = _gdMsg

    init {
        loadMessages()
    }

    fun getStudentByKey(key:String) = std.getStudentByKey(key)


    fun loadMessages() {
        _gdMsg.value = Resource.Loading
        db.collection("discussion")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _gdMsg.value = Resource.Failure(error.message.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val list = value.toObjects(MessageGDModel::class.java)
                    viewModelScope.launch {
                        list.forEach { m ->
                            m.user = getStudent(m.sender)
                            Log.d("TAG", "loadMessages: ${m.user}")
                        }
                        _gdMsg.value = Resource.Success(list)
                    }
                }
            }
    }

    private suspend fun getStudent(phone: String): StudentModel {
        return suspendCancellableCoroutine { continuation ->
            db.collection("students")
                .document(phone)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(StudentModel::class.java)
                    if (user != null) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(NullPointerException("User not found"))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }

            continuation.invokeOnCancellation {
                // Handle coroutine cancellation if needed
            }
        }
    }


    fun sendMessage(msg: String) {
        val msgModel = MessageGDModel(
            id = generateRandomString(22),
            message = msg,
            timestamp = System.currentTimeMillis(),
            sender = getStudentByKey("phone")!!,
            isEdited = false
        )

        db.collection("discussion")
            .document(msgModel.id)
            .set(msgModel)
            .addOnSuccessListener {
                // Success
            }
            .addOnFailureListener {
                // Failure
            }
    }
}
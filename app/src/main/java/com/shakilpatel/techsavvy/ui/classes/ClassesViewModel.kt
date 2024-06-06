package com.shakilpatel.techsavvy.ui.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.techsavvy.data.models.ClassModel
import com.shakilpatel.techsavvy.data.resources.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(
    private val db : FirebaseFirestore
) : ViewModel(){

    private val _classesList = MutableStateFlow<Resource<List<ClassModel>>?>(null)
    val classesList = _classesList

    init {
        getClassesList()
    }

    fun getClassesList()=viewModelScope.launch {
        _classesList.value = Resource.Loading
        db.collection("classes")
            .addSnapshotListener { value, error ->
                if(error!=null){
                    _classesList.value = Resource.Failure(error.message.toString())
                    return@addSnapshotListener
                }
                if(value!=null){
                    val classes = value.toObjects(ClassModel::class.java)
                    _classesList.value = Resource.Success(classes)
                }else{
                    _classesList.value = Resource.Failure("Something went wrong")
                }
            }
    }


}
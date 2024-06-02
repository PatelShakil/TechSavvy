package com.shakilpatel.techsavvy.data.models

data class MessageGDModel(
    val id : String,
    val message : String,
    val timestamp: Long,
    val sender:String,
    val isEdited:Boolean,
    var user:StudentModel? = null
){
    constructor():this("","",System.currentTimeMillis(),"",false)
}

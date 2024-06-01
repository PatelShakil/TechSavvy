package com.shakilpatel.techsavvy.data.models

data class StudentModel(
    val uid: String,
    val name:String,
    val phone:String,
    val email:String,
    val pin:String,
    val gender:String,
    val issub:Boolean
){
    constructor():this("","","","","","",false)
}

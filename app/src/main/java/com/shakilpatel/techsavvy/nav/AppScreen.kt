package com.shakilpatel.techsavvy.nav

sealed class AppScreen(val title : String = "",val route : String){


    object Auth:AppScreen("Auth","nav_auth"){
        object Login : AppScreen("Login","login")
    }

    object Home:AppScreen("Home","nav_home"){
        object Landing:AppScreen("Landing","landing")
    }
    object Profile:AppScreen("Profile","nav_profile"){
        object Landing:AppScreen("Landing","landing")
    }

    object Discussion : AppScreen("Discussion","nav_discussion"){
        object Landing:AppScreen("Landing","landing")
    }
    object Classes : AppScreen("Classes","nav_classes"){
        object Landing:AppScreen("Landing","landing")
    }

    object Students : AppScreen("Students","nav_students"){
        object Landing:AppScreen("Landing","landing")
    }

}
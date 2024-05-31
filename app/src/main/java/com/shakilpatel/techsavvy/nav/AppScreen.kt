package com.shakilpatel.techsavvy.nav

sealed class AppScreen(val title : String = "",val route : String){


    object Auth:AppScreen("Auth","nav_auth"){
        object Login : AppScreen("Login","login")
    }



}
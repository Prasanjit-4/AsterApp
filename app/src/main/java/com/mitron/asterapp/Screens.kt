package com.mitron.asterapp

sealed class Screens(val route:String){
    object SplashScreen:Screens("main_screen")
    object dataScreen:Screens("data_view_screen")
}

package com.example.myapplication2.model


data class User (val id:String,val name:String,val phone:String,val email:String){
    constructor():this("","","","")
}
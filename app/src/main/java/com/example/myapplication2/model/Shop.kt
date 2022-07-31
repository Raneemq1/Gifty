package com.example.myapplication2.model

data class Shop (val id:String,val name:String,val address:String,val phone:String,val email:String,val categories:ArrayList<String>){
    constructor() : this("","","","","", ArrayList<String>())

}

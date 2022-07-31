package com.example.myapplication2.model


data class Item (val id:String,val name:String,val price:Int,val quantity:Int,val category:String,val description:String,val pathImage:String,val email:String) {
    constructor():this("","",0,0,"","","","")
}
// add shop email
package com.example.myapplication2.model

data class Cart(val id:String,val userEmail:String,val requestedQuantity:Int,val itemId:String) {
    constructor ():this("","",0,"")
}
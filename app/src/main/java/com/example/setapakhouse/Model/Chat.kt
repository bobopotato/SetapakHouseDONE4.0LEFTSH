package com.example.setapakhouse.Model

class Chat(var message:String,var receiver:String,var sender:String,var isseen:String,var messageDate:String) {
    constructor():this("","","","","")
}
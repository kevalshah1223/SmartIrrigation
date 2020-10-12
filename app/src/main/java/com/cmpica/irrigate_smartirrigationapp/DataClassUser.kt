package com.cmpica.irrigate_smartirrigationapp

data class DataClassUser(var user_id: String, var user_name: String, var user_gen: String, var user_phone: String){
    constructor():this("","","","")
}
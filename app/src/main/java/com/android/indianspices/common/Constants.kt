package com.android.indianspices.common

import com.google.firebase.auth.FirebaseAuth

class Constants
{
    companion object{

        val PAYPAL_CLIENT_ID="Afbvj5ao-JaU4_HWaPtV_gmUWB9IQT9CNTbBB5jlKqop_5FBRcdVpD8nILz7M_SO5vE7WH1WNist7gSR"
        val userId= FirebaseAuth.getInstance().uid
        var username:String?=null
        var userphone:String?=null
        var userEmail:String?=null
        val mapFoodStatusCodeToFoodStatusText = mapOf("0" to "Order Placed", "1" to "Order Preparing" , "2" to "Order Completed")
         val DUPLICATE_USER_MESSAGE = "User with this email already exist."
         val USERS_KEY = "users"
         val REQUIRED_FIELDS_MESSAGE = "All fields are required"
         val EMAIL_KEY = "email"
         val PASSWORD_KEY = "password"
         val PASSWORD_LENGTH_MESSAGE = "Password must be at least 6 chars long"
        var foodListIds=ArrayList<String>()


    }




}
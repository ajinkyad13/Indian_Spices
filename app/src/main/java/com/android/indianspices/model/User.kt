package com.android.indianspices.model

import android.provider.ContactsContract

class User
{
    var userId:String?=null
    var name:String?=null
    var phone:String?=null
    var email:String?=null
    var role:String?=null
    var password:String?=null


    constructor(userId:String?=null,name:String,phone:String,email:String,password:String,role:String?=null)
    {
        this.userId=userId
        this.name=name
        this.phone=phone
        this.email=email
        this.password=password
        this.role="normal"

    }
    constructor()

}

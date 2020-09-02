package com.android.indianspices.model

import java.text.SimpleDateFormat
import java.util.*

class Request
{

    var requestID:String?=null
    var name:String?=null
    var phone:String?=null
    var total:String?=null
    var dateTime:String?=null
    var orders:List<Orders>?=null
    var paymentState:String?=null
    var status:String?=null

    constructor()

    constructor(userName:String?,userPhone:String?,total:String?,foodOrders:List<Orders>?,dateTime:String?,status:String?,paymentState:String?)
    {

        this.name=userName
        this.phone=userPhone
        this.total=total
        this.orders=foodOrders
        this.dateTime= dateTime
        this.status=status
        this.paymentState=paymentState

    }




}
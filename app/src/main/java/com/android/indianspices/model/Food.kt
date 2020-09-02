package com.android.indianspices.model

class Food
{
    var id:String?=null
    var name:String?=null
    var image:String?=null
    var description:String?=null
    var price:String?=null
    var discount:String?=null
    var menuid:String?=null
    constructor(name:String?=null,image:String,description:String,price:String,discount:String,menuId:String)
    {
        this.name=name
        this.image=image
        this.description=description
        this.price=price
        this.discount=discount
        this.menuid=menuId


    }
    constructor()

}
package com.android.indianspices.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "order_detail")
public final class Orders(@ColumnInfo(name = "product_id")var productID: String?=null,@ColumnInfo(name="product_name")var productName:String?=null,@ColumnInfo(name = "quantity") var quantity: String?="0",@ColumnInfo(name = "price")var price: String?="0" )
{
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    override fun toString(): String
    {
        return "$productName \t Qty:"+" "+"$quantity \t price:"+" "+"$$price"
    }




}
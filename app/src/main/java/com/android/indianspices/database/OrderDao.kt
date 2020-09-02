package com.android.indianspices.database

import androidx.room.*
import com.android.indianspices.model.Orders
import com.android.indianspices.model.User

@Dao
interface OrderDao
{
    @Query("SELECT * FROM ORDER_DETAIL") fun getAll(): List<Orders>
    @Insert fun insertAll(vararg orders: Orders)
    @Delete fun delete(order:Orders)
    @Query("DELETE FROM ORDER_DETAIL") fun deleteAll()


}
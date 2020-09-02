package com.android.indianspices.user.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.indianspices.R
import com.android.indianspices.adapter.ViewOrdersAdapter
import com.android.indianspices.model.Request
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.view_active_orders_screen.*
import java.util.ArrayList


class ViewActiveOrdersActivity :  AppCompatActivity()
{

    lateinit var orderList: ArrayList<Request>
    lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_active_orders_screen)

        orderList = ArrayList<Request>()

        val ordersDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests")

        activeOrderRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        var viewOrdersAdapter =   ViewOrdersAdapter(orderList)
        activeOrderRecyclerView.adapter = viewOrdersAdapter


        // read the values from firebase database
        ordersDatabaseRef.orderByKey().addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists())
                {
                    orderList.clear()
                    for( h in p0.children)
                    {
                        val eachOrder = h.getValue(Request::class.java)
                        if (eachOrder != null) {
                            eachOrder.requestID = h.key
                            if (eachOrder.status != "2")
                            {
                                orderList.add(eachOrder!!)
                            }
                        }
                    }
                }
                viewOrdersAdapter.notifyDataSetChanged()
            }

        })

    }
}
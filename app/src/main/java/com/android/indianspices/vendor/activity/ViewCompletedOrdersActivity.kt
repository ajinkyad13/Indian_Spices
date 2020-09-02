package com.android.indianspices.user.activity

import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.indianspices.R
import com.android.indianspices.adapter.ViewCompletedOrdersAdapter
import com.android.indianspices.model.Request
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.view_completed_orders_screen.*
//import kotlinx.android.synthetic.main.view_active_orders_screen.*
import java.util.ArrayList

class ViewCompletedOrdersActivity :  AppCompatActivity()
{

    lateinit var completedOrderList: ArrayList<Request>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_completed_orders_screen)

        completedOrderList = ArrayList<Request>()

        val ordersDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests")

        completdOrderRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        var viewCompletedOrdersAdapter =   ViewCompletedOrdersAdapter(completedOrderList)
        completdOrderRecyclerView.adapter = viewCompletedOrdersAdapter


        // read the values from firebase database
        ordersDatabaseRef.orderByKey().addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists())
                {
                    for( h in p0.children)
                    {
                        val eachOrder = h.getValue(Request::class.java)
                        if (eachOrder != null) {
                            eachOrder.requestID = h.key
                            if (eachOrder.status == "2")
                            {
                                completedOrderList.add(eachOrder!!)
                            }
                        }
//                        orderList.add(eachOrder!!)
                    }
                }
                viewCompletedOrdersAdapter.notifyDataSetChanged()
            }

        })

    }
}
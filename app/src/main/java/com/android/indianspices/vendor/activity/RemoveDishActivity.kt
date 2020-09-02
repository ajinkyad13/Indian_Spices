package com.android.indianspices.user.activity

import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.indianspices.R
import com.android.indianspices.adapter.RemoveDishesAdapter
import com.android.indianspices.adapter.ViewOrdersAdapter
import com.android.indianspices.model.Food
import com.android.indianspices.model.Request
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.remove_dishes_screen.*
import java.util.ArrayList

class RemoveDishActivity : AppCompatActivity()
{

    lateinit var foodList: ArrayList<Food>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remove_dishes_screen)

        foodList = ArrayList<Food>()

        val ordersDatabaseRef = FirebaseDatabase.getInstance().getReference("Foods")

        removeDishesRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        var removeDishesAdapter =   RemoveDishesAdapter(foodList)
        removeDishesRecyclerView.adapter = removeDishesAdapter


        // read the values from firebase database
        ordersDatabaseRef.orderByKey().addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists())
                {
                    foodList.clear()
                    for( h in p0.children)
                    {
                        val eachOrder = h.getValue(Food::class.java)
                        if (eachOrder != null) {
                                eachOrder.id = h.key
                                foodList.add(eachOrder!!)
                        }
                    }
                }
                removeDishesAdapter.notifyDataSetChanged()
            }

        })

    }
}
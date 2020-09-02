package com.android.indianspices.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.common.Constants
import com.android.indianspices.database.AppDatabase
import com.android.indianspices.model.Food
import com.android.indianspices.model.Orders
import com.android.indianspices.user.activity.HomeScreenActivity
import com.android.indianspices.user.activity.ui.dashboard.DashboardFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FoodCartViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    var foodName: TextView =view.findViewById(R.id.foodname_cart)
    var foodPrice: TextView =view.findViewById(R.id.foodprice_cart)
    var foodQty: TextView =view.findViewById(R.id.foodqty_cart)
    var requestDelete:ImageView=view.findViewById(R.id.deleterequest_cart)


}

class  FoodCartAdapter(private  var foodCartList:MutableList<Orders> ): RecyclerView.Adapter<FoodCartViewHolder>(){

    val databaseReference = FirebaseDatabase.getInstance().getReference("Foods")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodCartViewHolder
    {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_item,parent,false)
        return FoodCartViewHolder(view)

    }

    override fun getItemCount(): Int
    {

        return if(foodCartList.isNotEmpty())foodCartList.size else 0

    }


    override fun onBindViewHolder(holder: FoodCartViewHolder, position: Int)
    {
        var isRecordDeleted = true
        val order = foodCartList[position]





        val postListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                var food = dataSnapshot.getValue(Food::class.java)

                holder.foodName.text = food?.name
                holder.foodPrice.text = food?.price
                holder.foodQty.text = order.quantity



            }

            override fun onCancelled(databaseError: DatabaseError)
            {


            }
        }

        databaseReference.child(order.productID.toString())
            .addListenerForSingleValueEvent(postListener)

        holder.requestDelete.setOnClickListener(View.OnClickListener { view ->

            GlobalScope.launch {
                val db = AppDatabase.getInstance(view.context)
                val databaseAccess = db?.orderDao()

                databaseAccess?.delete(order)
                GlobalScope.launch(Dispatchers.Main) {
                    var dashboardFragment: DashboardFragment = DashboardFragment()
                    var fragmentTransaction: FragmentTransaction =
                        (view?.context as HomeScreenActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_dashboard, dashboardFragment)
                    fragmentTransaction.commit()
                }

            }


        })

        if(!Constants.foodListIds.contains(order.productID))
        {
            GlobalScope.launch {
                val db = AppDatabase.getInstance(holder.itemView.context)
                val databaseAccess = db?.orderDao()

                databaseAccess?.delete(order)}
            foodCartList.remove(order)





        }





    }





}
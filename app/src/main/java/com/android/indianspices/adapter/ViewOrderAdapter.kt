package com.android.indianspices.adapter

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.common.Constants
import com.android.indianspices.model.Food
import com.android.indianspices.model.Orders
import com.android.indianspices.model.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ViewOrderViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    var toggleButton:ImageView=view.findViewById(R.id.toggleButton)
    var requestID:TextView=view.findViewById(R.id.order_id)
    var status: TextView =view.findViewById(R.id.status)
    var date: TextView =view.findViewById(R.id.date)
    var totalcost: TextView =view.findViewById(R.id.totalcost)
    var requestedFood:ListView=view.findViewById(R.id.foodrequested_list)

}


class  ViewOrderAdapter(private  var viewOrderList:List<Request> ): RecyclerView.Adapter<ViewOrderViewHolder>(){

    companion object{
        var click_count=0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewOrderViewHolder
    {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_view_order,parent,false)
        return ViewOrderViewHolder(view)

    }

    override fun getItemCount(): Int
    {
        return if(viewOrderList.isNotEmpty())viewOrderList.size else 0

    }
    override fun onBindViewHolder(holder: ViewOrderViewHolder, position: Int)
    {
        val request=viewOrderList[position]
        holder.requestID.text="#"+request.requestID
        holder.date.text=request.dateTime.toString()
        holder.status.text=Constants.mapFoodStatusCodeToFoodStatusText.get(request.status.toString())?.toUpperCase()
        holder.totalcost.text="$"+request.total?.toDouble()
        holder.requestedFood.visibility=View.GONE

        var orders=ArrayList<Orders>()
        holder.toggleButton.setOnClickListener(View.OnClickListener{view->
            click_count++
            if(click_count%2==0)
            {
                holder.toggleButton.setImageDrawable(null)

                holder.toggleButton.setImageDrawable(view.context.getDrawable(R.drawable.ic_remove_circle_red_24dp))
                holder.requestedFood.visibility=View.VISIBLE
                holder.requestedFood.adapter =
                    ArrayAdapter(view.context, android.R.layout.simple_list_item_1,orders)

                if(!request.orders.isNullOrEmpty())
                {
                    orders.clear()
                    orders.addAll(request.orders!!)

                    GlobalScope.launch(Dispatchers.Main) {
                        var adapter = holder.requestedFood.adapter as? ArrayAdapter<Orders>
                        holder.requestedFood.layoutParams.height=orders.size*200
                        adapter?.notifyDataSetChanged()
                    }


                }


            }
            else{
                holder.toggleButton.setImageDrawable(null)
                holder.requestedFood.visibility=View.GONE
                holder.toggleButton.setImageDrawable(view.context.getDrawable(R.drawable.ic_add_circle_green_24dp))
            }
        })




    }




}
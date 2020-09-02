package com.android.indianspices.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.model.Orders
import com.android.indianspices.model.Request
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ViewCompletedOrdersViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    val orderNumber: TextView =   view.findViewById(R.id.completedOrderNumberField)
    val orderDate: TextView =   view.findViewById(R.id.completedOrderDateField)
    val orderUserName: TextView =   view.findViewById(R.id.completedOrderUserNameField)
    val foodList: ListView =   view.findViewById(R.id.completedAllFoods)
    val totalBill: TextView =   view.findViewById(R.id.completedtotalBillField)
    var customerPhone: TextView = view.findViewById(R.id.completedOrderCustomerPhoneField)

    val view: View = view.findViewById(R.id.completedAllFoodsLayout)
    val layout: LinearLayout = view.findViewById(R.id.completedAllFoodsLayout)
    val params: ViewGroup.LayoutParams = layout.layoutParams

}

class ViewCompletedOrdersAdapter(private val completedOrderList: List<Request>) :  RecyclerView.Adapter<ViewCompletedOrdersViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewCompletedOrdersViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.each_completed_order,parent,false)
        return ViewCompletedOrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(completedOrderList.isNotEmpty())completedOrderList.size else 0
    }

    override fun onBindViewHolder(holder: ViewCompletedOrdersViewHolder, position: Int) {
        ////////////
        val order=completedOrderList[position]
        var allOrders=ArrayList<Orders>()

        holder.orderNumber.text = order.requestID.toString()
        holder.orderDate.text = order.dateTime.toString()
        holder.orderUserName.text = order.name.toString()
        holder.totalBill.text = "$" + order.total.toString()
        holder.customerPhone.text = order.phone.toString()

        // Attaching the holder
        holder.foodList.adapter = ArrayAdapter(holder.orderNumber.context, android.R.layout.simple_list_item_1,allOrders)

        // populate the foods list view
        if(!order.orders.isNullOrEmpty())
        {
            allOrders.clear()
            allOrders.addAll(order.orders!!)

            holder.params.height = allOrders.size * 170
            holder.layout.layoutParams = holder.params

            GlobalScope.launch(Dispatchers.Main) {
                var adapter = holder.foodList.adapter as? ArrayAdapter<Orders>
                adapter?.notifyDataSetChanged()
            }
        }

    }

}
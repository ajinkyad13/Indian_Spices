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


class ViewOrdersViewHolder(view: View): RecyclerView.ViewHolder(view)
{
        val orderNumber: TextView   =   view.findViewById(R.id.orderNumberField)
        val orderDate: TextView     =   view.findViewById(R.id.orderDateField)
        val orderUserName: TextView =   view.findViewById(R.id.orderUserNameField)
        val foodList: ListView      =   view.findViewById(R.id.allFoods)
        val totalBill: TextView     =   view.findViewById(R.id.totalBillField)
        val phoneNumber : TextView = view.findViewById(R.id.orderUserPhoneField)

        val updateBtn: Button       =   view.findViewById(R.id.updateStatusBtn)
        val orderPlacedRadioBtn : RadioButton = view.findViewById(R.id.orderPlacedRadioBtn)

        val radioGroup:RadioGroup = view.findViewById(R.id.orderStatusRadioGroup)
        val view: View = view.findViewById(R.id.allFoodsLayout)

        val layout: LinearLayout = view.findViewById(R.id.allFoodsLayout)
        val params: ViewGroup.LayoutParams = layout.layoutParams

}

class ViewOrdersAdapter(private val orderList: List<Request>) :  RecyclerView.Adapter<ViewOrdersViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewOrdersViewHolder {

        val view= LayoutInflater.from(parent.context).inflate(R.layout.each_active_order,parent,false)
        return ViewOrdersViewHolder(view)


    }

    override fun getItemCount(): Int {
        return if(orderList.isNotEmpty())orderList.size else 0
    }

    override fun onBindViewHolder(holder: ViewOrdersViewHolder, position: Int) {

        ////////////
        val order=orderList[position]
        var allOrders=ArrayList<Orders>()

        holder.orderNumber.text = order.requestID.toString()
        holder.orderDate.text = order.dateTime.toString()
        holder.orderUserName.text = order.name.toString()
        holder.totalBill.text = "$"+order.total.toString()
        holder.phoneNumber.text = order.phone.toString()

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

        // Set current status
        when {
            order.status == "0" -> holder.radioGroup.check(R.id.orderPlacedRadioBtn)
            order.status == "1" -> {
                holder.orderPlacedRadioBtn.isEnabled = false
                holder.radioGroup.check(R.id.orderPreparingRadioBtn)
            }
            order.status == "2" -> holder.radioGroup.check(R.id.orderCompletedRadioBtn)
        }

            // on change status

        // Update the status

        // on change status
        var foodStatus = "-1"
        holder.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = holder.radioGroup.findViewById(checkedId)
            when {
                "${radio.text}" == "Order Placed" -> foodStatus = "0"
                "${radio.text}" == "Order Preparing" -> foodStatus = "1"
                "${radio.text}" == "Order Completed" -> foodStatus = "2"
            }
        }

        // Update the status
        holder.updateBtn.setOnClickListener() {
            val ordersDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests")
            order.requestID?.let { it1 -> ordersDatabaseRef.child(it1).child("status").setValue(foodStatus) }
            Toast.makeText(holder.updateBtn.context,"Order Status updated",Toast.LENGTH_SHORT).show()
        }
    }
}

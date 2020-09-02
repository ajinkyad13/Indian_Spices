package com.android.indianspices.user.activity.ui.notifications

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.android.indianspices.R
import com.android.indianspices.adapter.ViewOrderAdapter
import com.android.indianspices.common.Constants
import com.android.indianspices.model.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ViewAllOrdersFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val root = inflater.inflate(R.layout.fragment_view_all_orders, container, false)
        val databaseReferenceToRequest = FirebaseDatabase.getInstance().getReference("Requests")
        var firebaseAuth= FirebaseAuth.getInstance()
        var user=firebaseAuth.currentUser
        var phoneNumber=user?.uid

        var allOrders=ArrayList<Request>()
        val requestListRecyclerView: RecyclerView =root.findViewById(R.id.viewCompletedOrder_recycler)
        requestListRecyclerView.layoutManager=  LinearLayoutManager(this.activity,
            LinearLayoutManager.VERTICAL,false)
        var requestListAdapter= ViewOrderAdapter(allOrders)
        requestListRecyclerView.adapter=requestListAdapter

        val postListener=object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                for (child in dataSnapshot.children)
                {
                    val request = child.getValue(Request::class.java)


                    if(request!=null && (request.status=="2"))
                    {
                        request.requestID=child.key
                        allOrders.add(request)
                    }


                    requestListAdapter.notifyDataSetChanged()

                }


            }
            override fun onCancelled(databaseError: DatabaseError)
            {

            }

        }
        databaseReferenceToRequest.orderByChild("phone").equalTo(Constants.userphone).addListenerForSingleValueEvent(postListener)





        return root
    }

}

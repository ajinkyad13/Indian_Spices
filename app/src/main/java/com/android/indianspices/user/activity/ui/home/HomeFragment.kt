package com.android.indianspices.user.activity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.adapter.CategoryListAdapter
import com.android.indianspices.common.Constants
import com.android.indianspices.model.FoodCategory
import com.android.indianspices.user.activity.HomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment()
{


    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var categoryListView:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        firebaseAuth=FirebaseAuth.getInstance()

        var categoryList=ArrayList<FoodCategory>()
        val databaseReferenceForFood = FirebaseDatabase.getInstance().getReference("Foods")


        val categoryListView:RecyclerView=root.findViewById(R.id.recycler_category)
        categoryListView.layoutManager= LinearLayoutManager(this.activity,LinearLayoutManager.VERTICAL,false)
        var categoryListAdapter=CategoryListAdapter(categoryList)
        categoryListView.adapter=categoryListAdapter
        val postListenerForFoodId = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                Constants.foodListIds.clear()
                for(child in dataSnapshot.children)
                {
                   Constants.foodListIds.add(child.key.toString())
                }


            }

            override fun onCancelled(databaseError: DatabaseError)
            {


            }
        }

        databaseReferenceForFood.addListenerForSingleValueEvent(postListenerForFoodId)



        val databaseReference = FirebaseDatabase.getInstance().getReference("Category")
        val postListener=object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(child in dataSnapshot.children)
                {
                    val food=child.getValue(FoodCategory::class.java)

                    categoryList.add(food!!)


                }


                categoryListAdapter.notifyDataSetChanged()

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseReference.addListenerForSingleValueEvent(postListener)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }


}
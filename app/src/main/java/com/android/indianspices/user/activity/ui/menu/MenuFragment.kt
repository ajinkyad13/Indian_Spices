package com.android.indianspices.user.activity.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.adapter.FoodListAdapter
import com.android.indianspices.common.Constants
import com.android.indianspices.model.Food
import com.android.indianspices.model.FoodCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuFragment : Fragment()
{


    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        retainInstance=true

        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        val databaseReferenceToFoods = FirebaseDatabase.getInstance().getReference("Foods")
        val databaseReferenceToCategory = FirebaseDatabase.getInstance().getReference("Category")
        val menuHeader:TextView=root.findViewById(R.id.menu_heading)
        var foodList=ArrayList<Food>()
        val foodListRecyclerView:RecyclerView=root.findViewById(R.id.recycler_foodList)
        foodListRecyclerView.layoutManager= LinearLayoutManager(this.activity,LinearLayoutManager.VERTICAL,false)
        var foodListAdapter=FoodListAdapter(foodList)
        foodListRecyclerView.adapter=foodListAdapter
        var isArgumentPresent=false
        var position:Int=0
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

        databaseReferenceToFoods.addListenerForSingleValueEvent(postListenerForFoodId)

       /* if(this.arguments!=null)
        {
            position=arguments!!.getInt("position")
            isArgumentPresent=true

        }*/
        arguments?.let {
            val safeargs=MenuFragmentArgs.fromBundle(it)
            position=safeargs.categoryID
            isArgumentPresent=true
            val postListener=object : ValueEventListener
            {
                override fun onDataChange(dataSnapshot: DataSnapshot) {


                val category=dataSnapshot.getValue(FoodCategory::class.java)

                    menuHeader.text=category?.name

                }


                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            databaseReferenceToCategory.child("0"+(

                    position+1).toString()).addListenerForSingleValueEvent(postListener)
        }

        val postListener=object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                foodList.clear()

                for(child in dataSnapshot.children)
                {
                    if(isArgumentPresent)
                    {
                        val food = child.getValue(Food::class.java)
                        food?.id=child.key.toString()
                        if(food!=null)
                        {

                            if((food.menuid)?.toInt()==(position+1))
                            foodList.add(food!!)
                        }

                    }
                    else
                    {
                        val food = child.getValue(Food::class.java)
                        food?.id=child.key.toString()

                        foodList.add(food!!)
                    }
                    //val user= child.getValuevalue<User>()


                }


               foodListAdapter .notifyDataSetChanged()

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseReferenceToFoods.addListenerForSingleValueEvent(postListener)





        return root
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

    }
}
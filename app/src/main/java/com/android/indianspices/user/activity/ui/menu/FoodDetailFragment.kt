package com.android.indianspices.user.activity.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.android.indianspices.R
import com.android.indianspices.common.Constants
import com.android.indianspices.database.AppDatabase
import com.android.indianspices.model.Food
import com.android.indianspices.model.Orders
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FoodDetailFragment : Fragment()
{


   override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        retainInstance=true
        val root = inflater.inflate(R.layout.fragment_food_detail, container, false)
        val databaseReference = FirebaseDatabase.getInstance().getReference("Foods")
        var isArgumentPresent=false
        var foodId:String?=null
        if(this.arguments!=null)
        {
            foodId=arguments!!.getString("foodId")
            isArgumentPresent=true

        }
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

        databaseReference.addListenerForSingleValueEvent(postListenerForFoodId)

        var foodImage:ImageView=root.findViewById(R.id.img_food)
        var foodName:TextView=root.findViewById(R.id.foodNameText)
        var foodPrice:TextView=root.findViewById(R.id.foodPriceText)
        var btnCart:FloatingActionButton=root.findViewById(R.id.btnCart)
        var foodQuantity:ElegantNumberButton=root.findViewById(R.id.foodQuantity)
        var toolbar:androidx.appcompat.widget.Toolbar=root.findViewById(R.id.toolbar)
        var fooddescription:TextView=root.findViewById(R.id.food_description)

        var currFood:Food=Food()



        val postListener=object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                val food = dataSnapshot.getValue(Food::class.java)
                food?.id=dataSnapshot.key.toString()
                Picasso.get().load(food!!.image).error(R.drawable.chef_image).placeholder(R.drawable.chef_image).into(foodImage)
                foodPrice.text=food.price
                foodName.text=food.name
                fooddescription.text=food.description
               currFood= food


            }
            override fun onCancelled(databaseError: DatabaseError) {

        }
        }

        toolbar.setNavigationOnClickListener(View.OnClickListener { view->
            activity?.onBackPressed()
        })


        btnCart.setOnClickListener(View.OnClickListener {
        GlobalScope.launch {

            val currentOrderObj=Orders(currFood!!.id!!,currFood!!.name!!,foodQuantity.number,currFood.price)
            val db = AppDatabase.getInstance(root.context)
            val databaseAccess = db?.orderDao()
            databaseAccess?.insertAll(currentOrderObj)


        }
            Toast.makeText(context,"Added to Cart!!",Toast.LENGTH_SHORT).show()
        }
        )

        databaseReference.child(foodId!!).addListenerForSingleValueEvent(postListener)

                return root
    }












}

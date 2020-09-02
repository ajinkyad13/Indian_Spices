package com.android.indianspices.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.model.Food
import com.android.indianspices.model.Orders
import com.android.indianspices.model.Request
import com.android.indianspices.user.activity.RemoveDishActivity
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RemoveDishesViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    val foodName : TextView = view.findViewById(R.id.removeDishNameField)
    val foodPrice : TextView = view.findViewById(R.id.removeDishPriceField)
//    val foodDesc : TextView = view.findViewById(R.id.removeDishDescField)

    var foodThumbnail : ImageView =view.findViewById(R.id.removeDishFoodImage)
    var foodDelete : Button = view.findViewById(R.id.removeDishBtn)
    var holder: CardView = view.findViewById(R.id.viewAllOrders)

}

class RemoveDishesAdapter(private val foodList: MutableList<Food>) :  RecyclerView.Adapter<RemoveDishesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoveDishesViewHolder {

        val view= LayoutInflater.from(parent.context).inflate(R.layout.each_remove_dish,parent,false)
        return RemoveDishesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(foodList.isNotEmpty())foodList.size else 0
    }

    override fun onBindViewHolder(holder: RemoveDishesViewHolder, position: Int) {

        val food  =  foodList[position]
        val categories = R.array.foodCategories;

        Log.d("+++++++++++++++++++++++++++++++++++++", categories.toString())

        holder.foodName.text = food.name.toString()
        holder.foodPrice.text = "$"+ food.price.toString()
//        holder.foodDesc.text = food.description.toString()

        Picasso.get().load(food.image).error(R.drawable.chef_image).placeholder(R.drawable.chef_image).into(holder.foodThumbnail)

        holder.foodDelete.setOnClickListener(View.OnClickListener {view->
            val deleteSelectedFood = food.id?.let { FirebaseDatabase.getInstance().getReference("Foods").child(it) }
            deleteSelectedFood?.removeValue()
            foodList.removeAt(position)
            Toast.makeText(holder.foodDelete.context,"Dish Removed Successfully!",Toast.LENGTH_SHORT).show()
        })
    }
}
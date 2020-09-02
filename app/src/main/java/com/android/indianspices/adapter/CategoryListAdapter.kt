package com.android.indianspices.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation

import androidx.recyclerview.widget.RecyclerView

import com.android.indianspices.R
import com.android.indianspices.model.FoodCategory
import com.android.indianspices.user.activity.HomeScreenActivity
import com.android.indianspices.user.activity.ui.home.HomeFragmentDirections
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.squareup.picasso.Picasso

class CategoryListViewHolder(view: View): RecyclerView.ViewHolder(view)
{
    var categoryThumbnail: ImageView =view.findViewById(R.id.categoryImage)
    var categoryName: TextView =view.findViewById(R.id.categoryName)






}

class  CategoryListAdapter(private  var categoryList:List<FoodCategory> ): RecyclerView.Adapter<CategoryListViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder
    {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_food_category, parent, false)

        return CategoryListViewHolder(view)

    }

    override fun getItemCount(): Int
    {
        return if (categoryList.isNotEmpty()) categoryList.size else 0

    }

    fun loadNewData(newCategoryList: List<FoodCategory>)
    {
        categoryList = newCategoryList
        notifyDataSetChanged()
    }

    fun getFoodCategory(position: Int): FoodCategory?
    {
        return if (categoryList.isNotEmpty()) categoryList[position] else null
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int)
    {
        val category = categoryList[position]
        if(!category.image.isNullOrEmpty())
        {
            Picasso.get().load(category.image).error(R.drawable.chef_image)
                .placeholder(R.drawable.chef_image).into(holder.categoryThumbnail)
            holder.categoryName.text = category.name
        }
        else  Picasso.get().load(R.drawable.chef_image).into(holder.categoryThumbnail)


        holder.categoryThumbnail.setOnClickListener(View.OnClickListener {view->


           val onCategoryClick= HomeFragmentDirections.onCategoryClick(position)
                onCategoryClick.setCategoryID(position)
            Navigation.findNavController(view).navigate(onCategoryClick)




        })



    }


}
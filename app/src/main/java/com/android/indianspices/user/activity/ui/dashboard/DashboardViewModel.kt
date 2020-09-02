package com.android.indianspices.user.activity.ui.dashboard

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.indianspices.database.AppDatabase
import com.android.indianspices.model.Orders

class DashboardViewModel(@NonNull  application: Application) : ViewModel()
{
    private var db = AppDatabase.getInstance(application)
    var databaseAccess = db?.orderDao()
    lateinit var orderListLiveData:LiveData<List<Orders>>




    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }


    val text: LiveData<String> = _text




}
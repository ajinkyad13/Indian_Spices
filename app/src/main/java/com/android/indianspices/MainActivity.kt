package com.android.indianspices

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.reflect.Method
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.NetworkInfo
import android.widget.Toast
import com.android.indianspices.common.Constants
import com.android.indianspices.model.User
import com.android.indianspices.user.activity.HomeScreenActivity
import com.android.indianspices.user.activity.VendorHomeScreenActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity()
{

    lateinit var auth: FirebaseAuth
    var user: FirebaseUser?=null
    lateinit var connectivityManager:ConnectivityManager





    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth =FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser()
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        splashScreen.postDelayed(Runnable { ->
            if (isMobileDataEnabled())
            {

                if (user == null)
                {
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
                else{

                                        val postListener = object : ValueEventListener
                                        {
                                            override fun onDataChange(dataSnapshot: DataSnapshot)
                                            {

                                                val user = dataSnapshot.getValue(User::class.java)

                                                if (user != null && user?.role.equals("normal"))
                                                {
                                                    Constants.username=user.name
                                                    Constants.userphone=user.phone
                                                    Constants.userEmail=user.email
                                                    openHomeScreen()

                                                }
                                                else{
                                                    openVendorHomeScreen()
                                                }


                                            }


                                            override fun onCancelled(databaseError: DatabaseError)
                                            {

                                            }
                                        }

                                        databaseReference.child(user!!.uid).addListenerForSingleValueEvent(postListener)

                }

            }
        },3000)



    }

    public fun openHomeScreen()
{
    var intent=Intent(this,HomeScreenActivity::class.java)
    startActivity(intent)
    finish()


}

    fun openVendorHomeScreen()
    {
        var intent=Intent(this, VendorHomeScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

        fun isMobileDataEnabled():Boolean
        {
            var mobileDataEnabled: Boolean = false;
            connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

            if (activeNetwork?.isConnected != null)
            {
                return activeNetwork.isConnected
            }
            else
            {
                Toast.makeText(this,"Check your connection!!",Toast.LENGTH_SHORT).show()
                return false
            }
        }
}

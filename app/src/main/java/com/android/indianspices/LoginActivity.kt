package com.android.indianspices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.indianspices.common.Constants
import com.android.indianspices.model.User
import com.android.indianspices.user.activity.HomeScreenActivity
import com.android.indianspices.user.activity.VendorHomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()
{
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        signUpButton.setOnClickListener { _->
            startActivity(Intent(this, SignUpActivity::class.java))

        }
       loginButton.setOnClickListener {_->

            if((!loginEmailAddressText.text.toString().isNullOrEmpty()) && (!loginPasswordText.text.toString().isNullOrEmpty()))
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmailAddressText.text.toString(), loginPasswordText.text.toString())
                    .addOnCompleteListener(this) {task->
                        if(task.isSuccessful)
                        {
                            var userID=firebaseAuth.currentUser?.uid
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

                            databaseReference.child(userID!!).addListenerForSingleValueEvent(postListener)


                        }

                        else{
                            Toast.makeText(this, "Login unsuccessful", Toast.LENGTH_SHORT).show()
                        }

                    }
            }
           else {
                Toast.makeText(this, "Login unsuccessful: Empty Credentials-", Toast.LENGTH_SHORT).show()
            }
       }



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
}

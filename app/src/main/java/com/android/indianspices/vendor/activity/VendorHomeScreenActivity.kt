package com.android.indianspices.user.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.indianspices.LoginActivity
import com.android.indianspices.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.vendor_home_screen.*

class VendorHomeScreenActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vendor_home_screen)

        viewActiveOrdersBtn.setOnClickListener(){ _ ->
            startActivity(Intent(this, ViewActiveOrdersActivity::class.java))
        }

        viewCompletedOrders.setOnClickListener() { _ ->
            startActivity(Intent(this, ViewCompletedOrdersActivity::class.java))
        }

        addDishBtn.setOnClickListener() { _ ->
            startActivity(Intent(this, AddDishActivity::class.java))
        }

        removeDish.setOnClickListener() { _ ->
            startActivity(Intent(this, RemoveDishActivity::class.java))
        }

        vendorSignOutBtn.setOnClickListener() { _ ->
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}

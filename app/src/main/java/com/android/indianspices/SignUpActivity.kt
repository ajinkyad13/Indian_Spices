package com.android.indianspices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.android.indianspices.model.User
import com.android.indianspices.user.activity.HomeScreenActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity()
{
    lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        createAccountButton.setOnClickListener { _->
            if(textName.text?.trim().toString().isNullOrEmpty())
            {
                textName.error="Name can't be empty"
                textName.requestFocus()
            }

            else if(textNumber.text?.trim().toString().isNullOrEmpty())
            {
                textNumber.error = "Number cannot be empty"
                textNumber.requestFocus()
            }
            else if(textEmail.text?.trim().toString().isNullOrEmpty())
            {
                textEmail.error = "Email cannot be empty"
                textEmail.requestFocus()
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail.text?.trim()).matches()) {
                textEmail.error="Invalid Email"
                textEmail.requestFocus()
            }
            else if (textPassword.text?.trim().toString().length < 6) {
                textPassword.setError("Password should be greater than or equal to 6 characters");
                textPassword.requestFocus();

            }
            else if (textNumber.text?.trim().toString().length < 10) {
                textNumber.setError("Phone number cannot be less than 10 digits");
                textNumber.requestFocus();

            }
            else registerUser()
        }
        backToLoginButton.setOnClickListener { _ ->
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun registerUser()
    {
        val name:String=textName.text?.trim().toString()
        val phone=textNumber.text?.trim().toString()
        val email=textEmail.text?.trim().toString()
        val password=textPassword.text?.trim().toString()


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){

                task->
            if(task.isSuccessful)
            {
                var userID=mAuth.currentUser?.uid

                 val user:User=User(userID,name,phone,email,password)
                FirebaseDatabase.getInstance().getReference("users")
                    .child(userID.orEmpty()).setValue(user).addOnCompleteListener(this) {
                        subTask->
                        if (subTask.isSuccessful)
                        {
                            Toast.makeText(applicationContext, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_SHORT).show();

                        }


                    }

            }
            else{
                if (task.exception is FirebaseAuthUserCollisionException)
                    Toast.makeText(getApplicationContext(), "Email Already Exists!!", Toast.LENGTH_SHORT).show();
                else
                Toast.makeText(this, "Sign up unsuccessful", Toast.LENGTH_SHORT)
            }

        }


    }


}

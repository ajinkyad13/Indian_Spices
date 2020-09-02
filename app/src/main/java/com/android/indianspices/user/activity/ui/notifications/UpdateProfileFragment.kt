package com.android.indianspices.user.activity.ui.notifications


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.indianspices.R
import com.android.indianspices.common.Constants
import com.android.indianspices.model.User
import com.google.android.gms.common.internal.service.Common
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class UpdateProfileFragment : Fragment()
{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val root = inflater.inflate(R.layout.fragment_update_profile, container, false)
        val userId = FirebaseAuth.getInstance().uid
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val editName: TextInputEditText = root.findViewById(R.id.editTextName)
        val editEmail: TextInputEditText = root.findViewById(R.id.editTextEmail)
        val editphoneNumber: TextInputEditText = root.findViewById(R.id.editTextNumber)
        val updateButton:Button= root.findViewById(R.id.updateAccountButton)
        editName.setText(Constants.username.toString())
        editphoneNumber.setText(Constants.userphone.toString())
        editEmail.setText(Constants.userEmail.toString())


        updateButton.setOnClickListener { view ->
              editName.clearFocus()
            Constants.username=editName.text.toString()
            databaseReference.child(userId!!).child("name").setValue(editName.text.toString())
            Toast.makeText(root.context, "Name Successfully Edited", Toast.LENGTH_SHORT).show()


        }



        return root
    }


}

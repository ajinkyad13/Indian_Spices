package com.android.indianspices.user.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.android.indianspices.R
import com.android.indianspices.model.Food
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.add_dish.*
import java.util.*
import android.widget.LinearLayout
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

class AddDishActivity : AppCompatActivity()
{
    lateinit var dishName : EditText
    lateinit var dishDesc : EditText
    lateinit var dishPrice : EditText
    lateinit var  foodCategory : Spinner
    lateinit var uploadImage : Button
    lateinit var selectedCategory : String
    lateinit var  uploadedImage : ImageView

    lateinit var newlyAddedDishName : TextView
    lateinit var newlyAddedDishPrice : TextView
    lateinit var newlyAddedDishDesc : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_dish)

        dishName = findViewById(R.id.dishNameField)
        dishDesc = findViewById(R.id.descriptionField)
        dishPrice = findViewById(R.id.dishPriceField)
        foodCategory = findViewById(R.id.categorySpinner)
        uploadImage = findViewById(R.id.uploadImageBtn)
        uploadedImage = findViewById(R.id.uploadedImageView)


        newlyAddedDishName = findViewById(R.id.newlyAddedDishName)
        newlyAddedDishPrice = findViewById(R.id.newlyAddedDishPrice)
        newlyAddedDishDesc = findViewById(R.id.newlyAddedDishDesc)

        // Disable newly added dish layout
        val layout = findViewById<LinearLayout>(R.id.newlyAddedDishLayout)
        layout.visibility = View.GONE


        val categories = resources.getStringArray(R.array.foodCategories)

        foodCategory.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,categories)

        foodCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCategory = foodCategory.selectedItem.toString()
            }
        }

        //upload image button on click
        uploadImage.setOnClickListener() {
            // Disable newly added dish layout
            val layout = findViewById<LinearLayout>(R.id.newlyAddedDishLayout)
            layout.visibility = View.GONE

            //photo selector
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        // Add new dish button on click
        addNewDishBtn.setOnClickListener(){
            saveDish()
        }
    }

    var selectedPhotoUri : Uri? = null

    // After image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==0 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadedImageView.setBackgroundDrawable(bitmapDrawable)
            uploadImage.text = "Image Selected"
        }
    }

    private fun saveDish()
    {

        var name : String = dishName.text.toString().trim()
        var description : String = dishDesc.text.toString().trim()
        var price : String = dishPrice.text.toString().trim()
        var category: String = selectedCategory[0].toString()
        var uploadImageUri : String = ""

        // Food ID
        val ref = FirebaseDatabase.getInstance().getReference("Foods")

        // checking if name is given to the dish
        if(name.isEmpty()){
            dishName.error = "Please enter the dish name"
            return
        }

        if(description.isEmpty()){
            dishDesc.error = "Please enter the dish description"
            return
        }

        if(price.isEmpty()){
            dishPrice.error = "Please enter the dish price"
            return
        }

        if (selectedPhotoUri == null){
            Toast.makeText(applicationContext, "Please select food image", Toast.LENGTH_LONG).show()
            return
        }

        //        uploadImageToFirebaseStorage()
        val filename = UUID.randomUUID().toString()
        val ImageRef = FirebaseStorage.getInstance().getReference("/IndianSpicesImages/$filename")

        ImageRef.putFile(selectedPhotoUri!!).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
            ImageRef.getDownloadUrl().addOnSuccessListener(
                OnSuccessListener<Uri> { uri ->
                    uploadImageUri = uri.toString()
                    val dish = Food(name, uploadImageUri, description, price,"000", category)

                    ref.push().setValue(dish).addOnCompleteListener{
                        Toast.makeText(applicationContext, "Dish Saved Successfully", Toast.LENGTH_LONG).show()
                    }

                    // Disable newly added dish layout
                    val layout = findViewById<LinearLayout>(R.id.newlyAddedDishLayout)
                    layout.visibility = View.VISIBLE

                    // Display newly added dish data
                    newlyAddedDishName.text = name
                    newlyAddedDishPrice.text = "$"+price
                    newlyAddedDishDesc.text = description
                })
        })
    }

}

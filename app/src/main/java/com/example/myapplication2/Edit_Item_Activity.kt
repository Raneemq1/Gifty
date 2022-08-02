package com.example.myapplication2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication2.model.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Edit_Item_Activity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editPrice: EditText
    private lateinit var editQuantity: EditText
    private lateinit var editCategory: EditText
    private lateinit var editDescription: EditText
    private lateinit var editImage: ImageView
    private lateinit var itemId: String
    private lateinit var editBtn: Button
    private lateinit var updateBtn: Button
    private lateinit var ImageUri: Uri

    private var sRef = FirebaseStorage.getInstance().reference
    private val ref = FirebaseDatabase.getInstance().getReference("Items")

    private var shop_email_login: String = LoginActivity.shop_email
    private var shop_email_signup: String = SignUpShopActivity.shop_email
    private lateinit var email: String

    private var pathImage: String = ""
    private var path: String = ""
    private var pressed:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        itemId = intent.getStringExtra("itemDetails").toString()


        editName = findViewById(R.id.edit_itemName)
        editPrice = findViewById(R.id.edit_itemPrice)
        editCategory = findViewById(R.id.edit_itemCategory)
        editDescription = findViewById(R.id.edit_itemDescription)
        editQuantity = findViewById(R.id.edit_itemQuantity)
        editImage = findViewById(R.id.edit_item_Image)
        editBtn = findViewById(R.id.edit_item)
        updateBtn = findViewById(R.id.update_item)

        if (shop_email_login.isEmpty()) {
            email = shop_email_signup
        } else {
            email = shop_email_login
        }

        // Fill item information
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) { // check that db isn't empty
                    for (i in snapshot.children) {
                        val item = i.getValue(Item::class.java)
                        if (item?.id.equals(itemId)) {
                            editName.setText(item?.name)
                            editPrice.setText(item?.price.toString())
                            editCategory.setText(item?.category)
                            editQuantity.setText(item?.quantity.toString())
                            editDescription.setText(item?.description)
                            pathImage = item?.pathImage.toString()
                            sRef.child(pathImage).downloadUrl
                                .addOnSuccessListener { url ->

                                    path = url.toString()
                                    Glide.with(applicationContext).load(path).into(editImage)
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("TAG", "Exception: ${exception.message}")
                                }


                        }

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        editBtn.setOnClickListener {
            EditImage()
            pressed=1
        }


        updateBtn.setOnClickListener {
            val item_name: String
            val item_price: Int
            val item_quantity: Int
            val item_category: String
            var item_imageUri: String
            val item_description: String

            if (pressed==1) {

                val filepath = sRef?.child("Photos")?.child(Calendar.getInstance().time.toString())

                if (ImageUri != null) {
                    //Store image into storage
                    filepath?.putFile(ImageUri)?.addOnSuccessListener {

                    }
                }
                item_imageUri = filepath!!.path

                pressed=0
            } else {
                item_imageUri = pathImage

            }
            item_name = editName.text.toString()
            item_price = Integer.parseInt(editPrice.text.toString())
            item_quantity = Integer.parseInt(editQuantity.text.toString())
            item_description = editDescription.text.toString()
            item_category = editCategory.text.toString()
            val editable_item = Item(
                itemId,
                item_name,
                item_price,
                item_quantity,
                item_category,
                item_description,
                item_imageUri,
                email
            )
            ref.child(itemId).setValue(editable_item).addOnCompleteListener {
                Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show()

            }


        }
    }

    // Upload a new image from gallery
    private fun EditImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 2)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if the pick image process done well put UriImage to the image using setImageURI method
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK) {
            ImageUri = data?.data!!
            editImage.setImageURI(ImageUri)


        }
    }
}
package com.example.myapplication2.ui.addItem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication2.LoginActivity
import com.example.myapplication2.R
import com.example.myapplication2.SignUpShopActivity
import com.example.myapplication2.databinding.FragmentAddItemBinding
import com.example.myapplication2.model.Item
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

/**
 * This fragment displays items details to store it in the realtime database
 */
class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null

    lateinit var ImageUri: Uri
    lateinit var image: ImageView

    // This variable is used to check if the image button is clicked
    var pressed: Int = 0

    //These variables used to save the email of shop by taking an instance of these static variables
    private var shop_email_login: String = LoginActivity.shop_email
    private var shop_email_signup: String = SignUpShopActivity.shop_email
    private lateinit var email: String


    //This instance of firebase storage to save images
    private var mStoarge: StorageReference? = null

    private var firebaseStore: FirebaseStorage? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val AddItemViewModel =
            ViewModelProvider(this).get(AddItemViewModel::class.java)

        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /**
        Declare all widgets for adding item to firebase
         */
        val btn = root.findViewById<Button>(R.id.update_item)
        val select = root.findViewById<Button>(R.id.edit_item)
        val itemName = root.findViewById<EditText>(R.id.edit_itemName)
        val itemPrice = root.findViewById<EditText>(R.id.edit_itemPrice)
        val itemQuantity = root.findViewById<EditText>(R.id.edit_itemQuantity)
        val itemCategory = root.findViewById<EditText>(R.id.edit_itemCategory)
        val itemDescription = root.findViewById<EditText>(R.id.edit_itemDescription)
        image = root.findViewById<ImageView>(R.id.edit_item_image)

        // Save the email of shop by comparing the entry email from login and signup activity
        if (shop_email_login.isEmpty()) {
            email = shop_email_signup
        } else {
            email = shop_email_login
        }

        firebaseStore = FirebaseStorage.getInstance()
        mStoarge = FirebaseStorage.getInstance().reference

        /**
         * This is upload button listener to check fields before save them to db
         */
        btn.setOnClickListener {

            if (itemName.text.toString().isEmpty() || itemCategory.text.toString()
                    .isEmpty() || itemDescription.text.toString()
                    .isEmpty() || itemPrice.text.toString()
                    .isEmpty() || itemQuantity.text.toString()
                    .isEmpty() || !itemPrice.text.toString()
                    .isDigitsOnly() || !itemQuantity.text.toString()
                    .isDigitsOnly() || pressed == 0
            ) {
                Toast.makeText(
                    context,
                    "Please, fill information in a right way and select an image",
                    Toast.LENGTH_SHORT
                ).show()
                //check digits numbers

            } else {
                val filepath =
                    mStoarge?.child("Photos")?.child(Calendar.getInstance().time.toString())

                if (ImageUri != null) {

                    //Store image into storage
                    filepath?.putFile(ImageUri)?.addOnSuccessListener {

                    }
                    /**
                     * Create table called Items to save products details
                     */
                    var myDatabase = FirebaseDatabase.getInstance().getReference("Items")
                    val idItem = myDatabase.push().key.toString()
                    val item = Item(
                        idItem,
                        itemName.text.toString(),
                        Integer.parseInt(itemPrice.text.toString()),
                        Integer.parseInt(itemQuantity.text.toString()),
                        itemCategory.text.toString(),
                        itemDescription.text.toString(),
                        filepath!!.path,
                        email

                    )
                    myDatabase.child(idItem).setValue(item).addOnCompleteListener {
                    }

                    Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show()
                    itemName.setText(null)
                    itemPrice.setText(null)
                    itemCategory.setText(null)
                    itemDescription.setText(null)
                    itemQuantity.setText(null)
                    image.setImageResource(R.drawable.image)
                    pressed = 0


                }
            }

        }
        /**
         * Using intent to pick image from phone gallery
         */
        select.setOnClickListener {
            pressed = 1
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 2)

        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if the pick image process done well put UriImage to the image using setImageURI method
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK) {
            ImageUri = data?.data!!
            image.setImageURI(ImageUri)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
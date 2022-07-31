package com.example.myapplication2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication2.model.Shop
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList

class SignUpShopActivity : AppCompatActivity() {

    private var myAuth = FirebaseAuth.getInstance()

    private lateinit var shopName: EditText
    private lateinit var shopAddress: EditText
    private lateinit var shopPhone: EditText
    private lateinit var shopEmail: EditText
    private lateinit var shopPassword: EditText
    private lateinit var shopConfirmPassword: EditText
    private lateinit var signup: Button
    private lateinit var txt: TextView
    lateinit var categorySelected: BooleanArray
    private lateinit var categoryList: ArrayList<Int>
    private val categoryArray = arrayOf(
        "Accessories", "Antiques", "Perfume", "Books",
        "Electronics", "Men", "Women", "Children", "Toys",
        "Candles", "Painting", "Others"
    )
    private lateinit var categories: ArrayList<String>
    lateinit var builder: AlertDialog.Builder

    companion object {
        @JvmStatic
        var shop_email: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_shop)
        // Remove the status bar of phone
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        signup = findViewById(R.id.btn_signupshop)
        shopName = findViewById(R.id.text_ShopName)
        shopAddress = findViewById(R.id.text_shopAddress)
        shopPhone = findViewById(R.id.text_shopPhone)
        shopEmail = findViewById(R.id.text_shopEmail)
        shopPassword = findViewById(R.id.text_shopPassword)
        shopConfirmPassword = findViewById(R.id.text_shopConfirmPassword)
        txt = findViewById(R.id.relate_categories)
        builder = AlertDialog.Builder(this)
        categorySelected = BooleanArray(categoryArray.size)
        categoryList = ArrayList<Int>()
        categories = ArrayList<String>()


        txt.setOnClickListener {
            builder.setTitle("Select related categories");
            // set dialog non cancelable
            builder.setCancelable(false);
            builder.setMultiChoiceItems(
                categoryArray,
                categorySelected,
                DialogInterface.OnMultiChoiceClickListener { dialogInterface: DialogInterface, i: Int, b: Boolean ->
                    if (b) {
                        categoryList.add(i)
                        Collections.sort(categoryList)
                    } else {
                        categoryList.remove(i)
                    }
                })


            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                val stringBuilder = StringBuilder()
                for (i in 0..categoryList.size - 1) {
                    stringBuilder.append(categoryArray[categoryList.get(i)])
                    categories.add(categoryArray[categoryList.get(i)])
                    if (i != categoryList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                txt.setText(stringBuilder.toString())


            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss();
            })
            builder.setNeutralButton("Clear All", DialogInterface.OnClickListener { dialog, which ->
                for (j in 0..categorySelected.size - 1) {
                    // remove all selection
                    categorySelected[j] = false;
                    // clear language list
                    categoryList.clear();
                    // clear text view value
                    txt.setText("")
                }
            })
            builder.show()
        }

        signup.setOnClickListener {
            val name = shopName.text.toString()
            val address = shopAddress.text.toString()
            val category = shopPhone.text.toString()
            val email = shopEmail.text.toString()
            val password = shopPassword.text.toString()
            val confirmPassword = shopConfirmPassword.text.toString()

            if (name.isEmpty() || address.isEmpty() || category.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill your information", Toast.LENGTH_LONG).show()
            } else {
                if (password != confirmPassword) {
                    Toast.makeText(this, "Password mismatch", Toast.LENGTH_LONG).show()
                } else
                    signUp(
                        name.trim(),
                        address.trim(),
                        category.trim(),
                        email.trim(),
                        password.trim()
                    )
            }

        }
    }

    private fun signUp(
        name: String,
        address: String,
        category: String,
        email: String,
        password: String
    ) {
        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign up successfully", Toast.LENGTH_LONG).show()
                    saveUserData(name, address, category, email)
                    shop_email = email
                    val intent = Intent(this, ShopNavigationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Incorrect email address or password, please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }


    private fun saveUserData(name: String, address: String, category: String, email: String) {


        var myDatabase = FirebaseDatabase.getInstance().getReference("Shops")
        val idShop = myDatabase.push().key.toString()

        val shop = Shop(idShop, name, address, category, email, categories)
        myDatabase.child(idShop).setValue(shop).addOnCompleteListener {
            Toast.makeText(this, "Saved :)", Toast.LENGTH_LONG).show()
        }
    }


}
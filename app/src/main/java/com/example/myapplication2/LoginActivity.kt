package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.myapplication2.model.Shop
import com.example.myapplication2.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    // Create a reference to the real time database and authentication
    private var myAuth = FirebaseAuth.getInstance()
    private lateinit var ref1: DatabaseReference
    private lateinit var ref2: DatabaseReference

    // Declare used widgets
    private lateinit var login: Button
    private lateinit var signup: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var imageLock: ImageButton


    companion object {
        @JvmStatic
        var shop_email: String = ""
        var user_email: String = ""

    }

    // Initialize lock variable for controlling show/hide password
    private var lock: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login = findViewById(R.id.login)
        signup = findViewById(R.id.signup)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        imageLock = findViewById(R.id.img_lock)

        /**
         * Get a reference of the tables inside database
         * Shops , Users
         */
        ref1 = FirebaseDatabase.getInstance().getReference("Shops")
        ref2 = FirebaseDatabase.getInstance().getReference("Users")

        /**
         * Check all fields are fill to start sign in method checking
         */
        login.setOnClickListener { view ->
            val userEmail = email.text.toString()
            val userPassword = password.text.toString()
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                Toast.makeText(this, "Fill information", Toast.LENGTH_LONG).show()
            } else {
                signIn(view, userEmail.trim(), userPassword.trim())

            }

        }

        /**
         * Transfer to type_user  activity
         */
        signup.setOnClickListener {

            var intent = Intent(this, TypeActivity::class.java)
            startActivity(intent)
        }

        /**
         * Toggling the lock image with show/hide of password text
         */
        imageLock.setOnClickListener {
            if (lock == 0) {
                imageLock.setImageResource(R.drawable.ic_baseline_lock_open_24)
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                lock = 1
            } else {
                imageLock.setImageResource(R.drawable.ic_baseline_lock_24)
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                lock = 0
            }
        }

    }


    /**
     * Sign In With Email And Password using Authentication instance
     */
    private fun signIn(view: View, email: String, password: String) {
        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Log in successfully", Toast.LENGTH_LONG).show()
                    //Check if is a shop or user
                    typeOfUser(ref1, ref2, email)
                } else {
                    Toast.makeText(
                        this,
                        "Incorrect email address or password, please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })


    }


    /**
     * Check the entry type of user by checking the values for each table in the database
     */

    private fun typeOfUser(ref1: DatabaseReference, ref2: DatabaseReference, entryEmail: String) {


        ref1.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p: DataSnapshot) {
                if (p!!.exists()) { //Check that database isn't null

                    for (u in p.children) {
                        val shop = u.getValue(Shop::class.java)


                        // Check if the entry email related to shops emails
                        if (shop?.email.toString().equals(entryEmail)) {

                            shop_email = shop?.email.toString()

                            var intent =
                                Intent(applicationContext, ShopNavigationActivity::class.java)
                            startActivity(intent)

                            email.setText(null)
                            password.setText(null)
                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        ref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(m: DataSnapshot) {
                if (m!!.exists()) { // !! check that database isn't null

                    for (l in m.children) {
                        val user = l.getValue(User::class.java)

                        // Check if the entry email related to users emails
                        if (user?.email.toString().equals(entryEmail)) {
                            user_email=user?.email.toString()
                            var intent =
                                Intent(applicationContext, UserNavigationActivity::class.java)
                            startActivity(intent)

                            email.setText(null)
                            password.setText(null)

                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

}
package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button

class TypeActivity : AppCompatActivity() {

    lateinit var shopSignup: Button
    lateinit var userSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        shopSignup = findViewById(R.id.shopSignUp)
        userSignup = findViewById(R.id.userSignup)

        // Transfer to sign up process as shop
        shopSignup.setOnClickListener {

            var intent = Intent(this, SignUpShopActivity::class.java)
            startActivity(intent)
        }

        // Transfer to sign up process as normal user
        userSignup.setOnClickListener {

            var intent = Intent(this, SignUpUserActivity::class.java)
            startActivity(intent)
        }
    }
}
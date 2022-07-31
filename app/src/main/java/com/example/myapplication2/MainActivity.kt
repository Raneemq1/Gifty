package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val app_name: TextView = findViewById(R.id.app_name)
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.slide)



        app_name.startAnimation(animation1)


        Handler().postDelayed(
            {
                // Launch the Main Activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish() // Call this when your activity is done and should be closed.
            },
            1800 /*Time of Splash screen */
        )

    }

}
package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication2.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpUserActivity : AppCompatActivity() {

    private var myAuth = FirebaseAuth.getInstance()

    lateinit var signup: Button
    lateinit var userName: EditText
    lateinit var userPhone: EditText
    lateinit var userEmail: EditText
    lateinit var userPassword: EditText
    lateinit var userConfirmPassword: EditText

    companion object {
        @JvmStatic
        var user_email: String = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_user)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        signup = findViewById(R.id.btn_signupuser)
        userName = findViewById(R.id.text_userName)
        userPhone = findViewById(R.id.text_userPhone)
        userEmail = findViewById(R.id.text_userEmail)
        userPassword = findViewById(R.id.text_userPassword)
        userConfirmPassword = findViewById(R.id.text_userConfirmPassword)


        signup.setOnClickListener {


            val name = userName.text.toString()
            val phone = userPhone.text.toString()
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            val confirmPassword = userConfirmPassword.text.toString()

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill your information", Toast.LENGTH_LONG).show()
            } else {
                if (password != confirmPassword) {
                    Toast.makeText(this, "Password mismatch", Toast.LENGTH_LONG).show()
                } else
                    signUp(name.trim(), phone.trim(), email.trim(), password.trim())
            }

        }
    }


    private fun signUp(name: String, phone: String, email: String, password: String) {
        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign up successfully", Toast.LENGTH_LONG).show()
                    saveUserData(name, phone, email)
                    val intent = Intent(this, UserNavigationActivity::class.java)
                    user_email=email
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


    private fun saveUserData(name: String, phone: String, email: String) {


        var myDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val idUser = myDatabase.push().key.toString()

        val user = User(idUser, name, phone, email)
        myDatabase.child(idUser).setValue(user).addOnCompleteListener {
            Toast.makeText(this, "Saved :)", Toast.LENGTH_LONG).show()
        }
    }

}
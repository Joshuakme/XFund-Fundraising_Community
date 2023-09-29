package com.example.xfund

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.xfund.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User


class Login : AppCompatActivity() {
    private lateinit var loginBinding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userList: MutableList<User>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        //loginBinding.progressBarLogin.visibility = View.GONE

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("XFundDB")
        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        //'Forgot password?' clicked
        loginBinding.tvForgotPassword.setOnClickListener() {
            //verification through email, using firebase database authentication
            //navigate to forgot password page
            val intent = Intent(this, LoginForgotPassword::class.java)
            startActivity(intent)
        }

        //'Register' clicked
        loginBinding.tvRegisterAcc.setOnClickListener() {
            //navigate to registration page
            val intent = Intent(this, RegisterAccount::class.java)
            startActivity(intent)
        }

        //login button clicked
        loginBinding.btnLogin.setOnClickListener() {
            //validation
            //loginBinding.progressBarProfile.visibility =View.VISIBLE
            val username = loginBinding.tfLoginEmail.text.toString()
            val psw = loginBinding.tfLoginPassword.text.toString()
            if (username != "" && psw != "") {
                //loginBinding.progressBarLogin.visibility = View.VISIBLE
                getEmail(username, psw)
                //loginBinding.progressBarProfile.visibility =View.GONE
            } else {
                Toast.makeText(
                    applicationContext,
                    "Input field must not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    //when login button clicked, call this function to get the email using the username entered
    private fun getEmail(username: String, psw: String) {
        var email: String = ""

        database.child("User").child(username).get()
            .addOnSuccessListener { result ->
                if (result.child("email").value != null) {

                    email = result.child("email").value.toString()
                    userLogin(username, email, psw)
                } else {
                    Toast.makeText(applicationContext, "User not found", Toast.LENGTH_SHORT).show()
                    ///loginBinding.progressBarLogin.visibility = View.GONE
                }
            }
            .addOnFailureListener { ex ->
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
                //loginBinding.progressBarLogin.visibility = View.GONE
            }
    }

    //when the email found, login the user using email and password
    private fun userLogin(username: String, email: String, psw: String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                //update the password in case the user reset password
                database.child("User").child(username).child("password").setValue(psw)
                    .addOnSuccessListener {
                        //navigate to main page
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("Username", username)
                        startActivity(intent)
                        Toast.makeText(applicationContext, "Successfully login", Toast.LENGTH_SHORT)
                            .show()
                        //save to preference file / sharedPreference
                        val editor = sharedPreferences.edit()
                        editor.putString("User", username)
                        editor.commit()
                        //loginBinding.progressBarLogin.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            "Fail to update password",
                            Toast.LENGTH_SHORT
                        ).show()
                        //loginBinding.progressBarLogin.visibility = View.GONE
                    }
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
            }
            .addOnFailureListener { ex ->
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
                //loginBinding.progressBarLogin.visibility = View.GONE
            }
    }
}



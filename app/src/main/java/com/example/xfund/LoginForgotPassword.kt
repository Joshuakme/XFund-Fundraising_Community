package com.example.xfund

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.xfund.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class LoginForgotPassword : AppCompatActivity() {
    private lateinit var Binding: FragmentForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        auth = FirebaseAuth.getInstance()

        //send email button clicked
        Binding.btnResetSendEmail.setOnClickListener() {
            val email = Binding.tfResetEmail.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    applicationContext,
                    "Please enter your email address",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //send reset email
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Email sent. Please check your email",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        //'Back to login' clicked
        Binding.tvBackToLogin.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}
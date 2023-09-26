package com.example.xfund

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.xfund.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
//import kotlinx.android.synthetic.main.activity_registration.*
//import kotlinx.android.synthetic.main.activity_registration.view.*

class RegisterAccount : AppCompatActivity() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = FragmentRegisterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.progressBarRegistration.visibility = View.GONE
//
//        auth = FirebaseAuth.getInstance()
//        database =FirebaseDatabase.getInstance().getReference("MoneyTrackerDb")
//
//        //'Log in' clicked
//        binding.tvLogin.setOnClickListener() {
//            //navigate back to login page
//            val intent = Intent (this, Login::class.java)
//            startActivity(intent)
//        }
//
//        //register button clicked
//        binding.btnRegister.setOnClickListener() {
//            val email: String = binding.tfRegisterEmail.text.toString()
//            val psw: String = binding.tfRegisterPassword.text.toString()
//            val confirmPsw: String = binding.tfRegisterConfirmPassword.text.toString()
//            binding.progressBarRegistration.visibility = View.VISIBLE
//            //check if there is any empty input field
//            if (email != "" && psw != "" && confirmPsw != "") {
//                //check if email already exist
//                database.child("User").child(email).get()
//                    .addOnSuccessListener { result ->
//                        if (result.child("Email").value != null) {
//                            //if username already exist (used by other user)
//                            Toast.makeText(applicationContext, "Email is used by the other user, please change your Email", Toast.LENGTH_SHORT).show()
//                            binding.progressBarRegistration.visibility = View.GONE
//                        }
//                        else {
//                            //if email not yet exist,
//                            //check if password and confirm password has more than 6 characters
//                            if (psw.length >= 6 && confirmPsw.length >= 6) {
//                                //check if the password is same with the confirm password
//                                if (psw == confirmPsw) {
//                                    binding.progressBarRegistration.visibility = View.GONE
//                                    //navigate to SetupValuePage (SetupProfile)
//                                    val intent = Intent (this, SetupValuePage::class.java)
//                                    intent.putExtra("email", email)
//                                    intent.putExtra("password", psw)
//                                    startActivity(intent)
//                                }
//                                else {
//                                    Toast.makeText(applicationContext, "Password and Confirm Password must be the same", Toast.LENGTH_SHORT).show()
//                                    binding.progressBarRegistration.visibility = View.GONE
//                                }
//                            }
//                            else {
//                                Toast.makeText(applicationContext, "Password and Confirm Password must contain at least 6 characters", Toast.LENGTH_SHORT).show()
//                                binding.progressBarRegistration.visibility = View.GONE
//                            }
//                        }
//                    }
//                    .addOnFailureListener { ex ->
//                        Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
//                        binding.progressBarRegistration.visibility = View.GONE
//                    }
//            }
//            else {
//                Toast.makeText(applicationContext, "Input field must not be empty", Toast.LENGTH_SHORT).show()
//                binding.progressBarRegistration.visibility = View.GONE
//            }
//        }
    }
}
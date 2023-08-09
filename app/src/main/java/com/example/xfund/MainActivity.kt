package com.example.xfund

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
//    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    fun writeNewUser(userId: String, name: String, email: String) {
//        database = Firebase.database.reference
//
//        val user = User(name, email)
//
//        database.child("users").child(userId).setValue(user)
//    }
}
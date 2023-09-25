package com.example.xfund.screens.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Check if logged in
        val isLogin: Boolean = sharedPref?.getBoolean("IsLogin", false) == true
        if(!isLogin) {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            return view
        }

        // Else - do something

        return view
    }

}
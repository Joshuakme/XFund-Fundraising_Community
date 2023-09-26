package com.example.xfund.screens.navigation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        // Variables
        sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        auth = Firebase.auth


        // Check if logged ind
        val isLogin: Boolean = sharedPreferences?.getBoolean("IsLogin", false) == true
        if (!isLogin) {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            return view
        } else {
            // Variables
            val signOutBtn = view.findViewById<MaterialCardView>(R.id.signOutCard)


            // Event Listeners
            signOutBtn.setOnClickListener {
                auth.signOut()

                //save to preference file / sharedPreference
                val editor = sharedPreferences.edit()
                editor.putBoolean("IsLogin", false)
                editor.commit()

                // Display Message
                Toast.makeText(
                    context,
                    "Signed Out",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to Home Page
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            }

            //Click the 'Account" in Profile Fragment, it will link to Edit Profile Fragment
            view.findViewById<MaterialCardView>(R.id.profileCard).setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
        }


        // Else - do something

        return view
    }

}
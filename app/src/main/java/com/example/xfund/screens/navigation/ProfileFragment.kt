package com.example.xfund.screens.navigation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.xfund.R
import com.example.xfund.viewModel.UserViewModel
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: FirebaseUser
    private var userViewModel = UserViewModel()

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

        if(isLogin) {
            userViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    val imageUrl = user?.photoUrl   // Retrieve user's profile image URL from Firestore or Realtime Database
                    if(user.displayName.isNullOrEmpty()) {
                        view.findViewById<TextView>(R.id.ProfileName).text = "Username"
                    } else {
                        view.findViewById<TextView>(R.id.ProfileName).text = user.displayName
                    }

                    Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.baseline_account_circle)
                        .into(view?.findViewById(R.id.ProfileImage))
                }
            }

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
        } else {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            return view
        }

        return view
    }
}
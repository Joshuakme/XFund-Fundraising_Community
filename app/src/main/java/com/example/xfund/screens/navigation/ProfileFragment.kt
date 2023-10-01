package com.example.xfund.screens.navigation

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.xfund.R
import com.example.xfund.util.FirebaseHelper
import com.example.xfund.viewModel.UserViewModel
import com.google.android.gms.common.internal.BaseGmsClient.SignOutCallbacks
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private var currentUserViewModel = UserViewModel()
    private val firestoreRepository = FirebaseHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Variables
        sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        auth = Firebase.auth
        val isLogin: Boolean = sharedPreferences?.getBoolean("IsLogin", false) == true


        // Check if logged in
        if(!isLogin) {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Variables
        val profileBtn = view.findViewById<MaterialCardView>(R.id.profileCard)
        val viewDiscussionBtn = view.findViewById<MaterialCardView>(R.id.viewPostCard)
        val signOutBtn = view.findViewById<MaterialCardView>(R.id.signOutCard)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        // Check if the user is authenticated
        if (currentUser != null) {
            val uid = currentUser.uid

            currentUserViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    val imageUrl =
                        user?.photoUrl   // Retrieve user's profile image URL from Firestore or Realtime Database
                    if (user.displayName.isNullOrEmpty()) {
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

            // EVENT LISTENERS
            // "Account" button
            profileBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
            // View Posted Discussions
            viewDiscussionBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_viewPostedCommunityFragment)
            }
            // Sign Out
            signOutBtn.setOnClickListener {
                val messageSignOut: String = "Are you sure want to sign out?"
                showSignOutAccountDialog(messageSignOut)
            }
        }
    }

    private fun showSignOutAccountDialog(messageSignOut: String?){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.signout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessageSignOut: TextView = dialog.findViewById(R.id.tvMessageSignOut)
        val btnSignOut : Button = dialog.findViewById(R.id.btnSignOut)
        val btnCancelSignOut : Button = dialog.findViewById(R.id.btnCancelSignOut)

        tvMessageSignOut.text = messageSignOut

        btnSignOut.setOnClickListener{
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

            dialog.dismiss()
        }

        btnCancelSignOut.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }
}
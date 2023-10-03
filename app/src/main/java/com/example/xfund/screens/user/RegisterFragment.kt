package com.example.xfund.screens.user

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentRegisterBinding
import com.example.xfund.model.User
import com.example.xfund.viewModel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // reload
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // can navigate to another fragment or activity here
        // Example: navigate to another fragment
        //Navigate From Register Fragment to Home Fragment
        binding.RegisterBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        //Navigate From Register Fragment to Login Fragment
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            // Display Loading Dialog
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Registering...")
            progressDialog.setCancelable(false) // Prevent dismiss by tapping outside
            progressDialog.show()

            // Variables
            val email = binding.textInputEmail.editText?.text.toString()
            val password = binding.textInputPassword.editText?.text.toString()
            val confirmPassword = binding.textInputConPassword.editText?.text.toString()
            val userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    auth.createUserWithEmailAndPassword(email, password)
                        // Success Registration
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser

                                // Register to Firestore
                                registerToFirestore(auth.currentUser)

                                // Set UserViewModel
                                userViewModel.setUser(user)

                                // Set Toast Message
                                Toast.makeText(
                                    requireContext(), "Registered Successful", Toast.LENGTH_SHORT).show()

                                progressDialog.dismiss()

                                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

                                // Show the bottom navigation
                                val bottomNav =
                                    activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
                                if (bottomNav != null) {
                                    bottomNav.visibility = View.VISIBLE
                                }
                            }
                        }
                        .addOnFailureListener(requireActivity()) {
                            Toast.makeText(
                                requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password is not matching!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Empty Fields are not Allowed!",
                  Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()
            }
        }
    }

    private fun registerToFirestore(user: FirebaseUser?) {
        val db = Firebase.firestore

        // Create a User object with the user's details
        val newUser = User(
            uid = user?.uid ?: "",
            displayName = user?.displayName ?: "",
            email = user?.email ?: "",
            imgUri = user?.photoUrl
        )

        // Add the user details to Firestore
        db.collection("users").document(user?.uid ?: "")
            .set(newUser)
            .addOnSuccessListener {
                // User details added to Firestore successfully
            }
            .addOnFailureListener { e ->
                // Handle the error
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun reload() {
    }
}
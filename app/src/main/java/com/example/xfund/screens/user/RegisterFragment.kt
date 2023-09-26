package com.example.xfund.screens.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentRegisterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences


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
            val email = binding.textInputEmail.editText?.text.toString()
            val password = binding.textInputPassword.editText?.text.toString()
            val confirmPassword = binding.textInputConPassword.editText?.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser

                                sharedPref = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                                // Save Login Status (True) in Shared Preference
                                val editor = sharedPref.edit()
                                editor.putBoolean("IsLogin", true)
                                editor.apply()

                                // Set Toast Message
                                Toast.makeText(
                                    requireContext(), "Registered Successful", Toast.LENGTH_SHORT).show()

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
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password is not matching!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Empty Fields are not Allowed!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("EmailPassword", "createUserWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("EmailPassword", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    fun reload() {
    }
}
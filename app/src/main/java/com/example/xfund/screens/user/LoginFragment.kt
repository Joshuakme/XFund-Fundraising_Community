package com.example.xfund.screens.user

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentLoginBinding
import com.example.xfund.util.FirebaseHelper
import com.example.xfund.viewModel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private var _binding: FragmentLoginBinding? = null
        get() = _binding!!
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userViewModel : UserViewModel
    private val firestoreRepository = FirebaseHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,false)

        // VARIABLES
        auth = Firebase.auth        // Firebase Variables
        sharedPref = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)    // Shared Preference


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // VARIABLES
        // View Elements
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        emailEditText = binding.tfLoginEmail
        passwordEditText = binding.tfLoginPassword

        // State Variables
        var isValidEmail: Boolean = false

        // Init Layout Config
        bottomNav?.visibility = View.GONE   // Hide bottom nav when load this page
        binding.btnLogin.isEnabled = false


        // EVENT LISTENERS
        binding.tvRegisterAcc.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.LoginBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            // Show the bottom navigation
            bottomNav?.visibility = View.VISIBLE
        }

        emailEditText.addTextChangedListener {
            // Valid input field
            if (emailEditText.text.isNotEmpty() || emailEditText.text.isNotBlank() ||
                passwordEditText.text.isNotEmpty() || passwordEditText.text.isNotBlank()
            ) {

                // enable button
                binding.btnLogin.isEnabled = true

                // Check Email Format
                isValidEmail = checkEmailFormat(emailEditText.text.toString())
            }
            // Invalid input field
            else {
                // Display Error Message
                Toast.makeText(context, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            if (isValidEmail) {
                // Display Loading Dialog
                val builder = AlertDialog.Builder(this.requireContext())
                val dialog = builder.setTitle("Loading").create()
                dialog.show()

                // Sign in With (Email, Password)
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    val authResult = firestoreRepository.logInUser(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()
                    )

                    if (authResult != null) {
                        // User logged in successfully


                        // Set UserViewModel
                        userViewModel =
                            ViewModelProvider(requireActivity())[UserViewModel::class.java]
                        userViewModel.setUser(auth.currentUser)

                        // Save Login Status (True) in Shared Preference
                        val editor = sharedPref?.edit()
                        editor?.putBoolean("IsLogin", true)?.apply()

                        // Set Toast Message
                        Toast.makeText(context, "Login Successfully!", Toast.LENGTH_SHORT).show()

                        // Close the loading Alert Dialog after success login
                        dialog.dismiss()

                        // Navigate to Homepage
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                        // Show the bottom navigation
                        bottomNav?.visibility = View.VISIBLE
                    } else {
                        // Handle login failure


                        // Save Login Status (False) in Shared Preference
                        with(sharedPref?.edit()) {
                            this?.putBoolean("Islogin", false)?.apply()
                        }

                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
//                auth.signInWithEmailAndPassword(
//                    emailEditText.text.toString(),
//                    passwordEditText.text.toString()
//                )
//                    .addOnCompleteListener() { task ->
//                    // Set UserViewModel
//                    userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
//                    userViewModel.setUser(auth.currentUser)
//
//                    // Save Login Status (True) in Shared Preference
//                    val editor = sharedPref?.edit()
//                    editor?.putBoolean("IsLogin", true)?.apply()
//
//                    // Set Toast Message
//                    Toast.makeText(context,"Login Successfully!", Toast.LENGTH_SHORT).show()
//
//                    // Close the loading Alert Dialog after success login
//                    dialog.dismiss()
//
//                    // Navigate to Homepage
//                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//
//                    // Show the bottom navigation
//                    bottomNav?.visibility = View.VISIBLE
//                }
//                .addOnFailureListener {
//                    // Save Login Status (False) in Shared Preference
//                    with(sharedPref?.edit()) {
//                        this?.putBoolean("Islogin", false)?.apply()
//                    }
//
//                    Toast.makeText(context,"Authentication failed.",Toast.LENGTH_SHORT).show()
//                }
//            }
//            // Invalid Email Format
//            else {
//                // Display Error Message
//                Toast.makeText(context,"Invalid email address format", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
    }
    private fun checkEmailFormat(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}





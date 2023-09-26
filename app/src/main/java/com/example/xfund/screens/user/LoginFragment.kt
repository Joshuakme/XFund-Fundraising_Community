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
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private var _binding: FragmentLoginBinding? = null
        get() = _binding!!
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        // Hide bottom nav when load this page
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }

        binding.btnLogin.isEnabled = false


        // Initialize Variable
        auth = Firebase.auth        // Firebase Auth
        emailEditText = binding.tfLoginEmail
        passwordEditText = binding.tfLoginPassword
        sharedPref = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        var isValidEmail: Boolean = false

        // Event Listeners
        binding.tvRegisterAcc.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        // Check if input empty
        emailEditText.addTextChangedListener {
            // Valid input field
            if(emailEditText.text.isNotEmpty() || emailEditText.text.isNotBlank() ||
                passwordEditText.text.isNotEmpty() || passwordEditText.text.isNotBlank()) {

                // enable button
                binding.btnLogin.isEnabled = true

                // Check Email Format
                isValidEmail = checkEmailFormat(emailEditText.text.toString())
            }
            // Invalid input field
            else {
                // Display Error Message
                Toast.makeText(
                    context,
                    "Please fill in all fields!",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }


        // set on click listener
        binding.btnLogin.setOnClickListener {
            if(isValidEmail) {
                val builder = AlertDialog.Builder(this.requireContext())
                builder.setTitle("Loading")
                builder.show()

                auth.signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")

                        val user = auth.currentUser

                        // Save Login Status (True) in Shared Preference
                        val editor = sharedPref.edit()
                        editor.putBoolean("IsLogin", true)
                        editor.apply()

                        // Set Toast Message
                        Toast.makeText(
                            context,
                            "Login Successfully!",
                            Toast.LENGTH_SHORT,
                        ).show()

                        // Navigate to Homepage
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                        // Show the bottom navigation
                        val bottomNav =
                            activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
                        if (bottomNav != null) {
                            bottomNav.visibility = View.VISIBLE
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                    }
                }
                    .addOnFailureListener {
                        // Save Login Status (False) in Shared Preference
                        with(sharedPref?.edit()) {
                            this?.putBoolean("Islogin", false)
                            this?.apply()
                        }

                        Toast.makeText(
                            context,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
            }
            // Invalid Email Format
            else {
                // Display Error Message
                Toast.makeText(
                    context,
                    "Invalid email address format",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }


        binding.LoginBackBtn.setOnClickListener {
            // can navigate to another fragment or activity here
            // Example: navigate to another fragment
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            // Show the bottom navigation
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
            if (bottomNav != null) {
                bottomNav.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Reload successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "reload", task.exception)
                Toast.makeText(context, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isEmpty(etText: EditText): Boolean {
        return if (etText.text.toString().trim { it <= ' ' }.length > 0) false else true
    }

    private fun checkEmailFormat(email: String): Boolean {
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true
        } else {
            false
        }

        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





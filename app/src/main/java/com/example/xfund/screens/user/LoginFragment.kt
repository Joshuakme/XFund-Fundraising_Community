package com.example.xfund.screens.user

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentLoginBinding
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
    private val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        // Initialize Firebase Auth
        auth = Firebase.auth


        // Initialize Variable
        emailEditText = binding.tfLoginEmail
        passwordEditText = binding.tfLoginPassword

        binding.btnLogin.setOnClickListener {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Loading")
            builder.show()

            Toast.makeText(
                context,
                "Button Clicked",
                Toast.LENGTH_SHORT,
            ).show()

            auth.signInWithEmailAndPassword(emailEditText.toString(), passwordEditText.toString())
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser

                        // Save Login Status (True) in Shared Preference
                        with (sharedPref?.edit()) {
                            this?.putBoolean("Islogin", true)
                            this?.apply()
                        }

                        Toast.makeText(
                            context,
                            "Login Successfully!",
                            Toast.LENGTH_SHORT,
                        ).show()

                        // Navigate to Homepage
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                    }
                }
                .addOnFailureListener {
                    // Save Login Status (False) in Shared Preference
                    with (sharedPref?.edit()) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





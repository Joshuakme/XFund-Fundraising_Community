package com.example.xfund.screens.user

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Send email button clicked
        binding.btnResetSendEmail.setOnClickListener {
            val email = binding.tfResetEmail.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Please enter your email address", Toast.LENGTH_SHORT).show()
            } else {
                // Send reset email
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Email sent. Please check your email",
                                Toast.LENGTH_SHORT
                            ).show()
//                            val intent = Intent(requireContext(), LoginFragment::class.java)
//                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                task.exception?.message ?: "Password reset failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        // 'Back to login' clicked
        binding.tvBackToLogin.setOnClickListener {
                findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
            }

        binding.ResetPassBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
        }
    }


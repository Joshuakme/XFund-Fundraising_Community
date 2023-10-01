package com.example.xfund.util

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_dialog, container, false)

        // ELEMENTS
        val loginBtn = view.findViewById<Button>(R.id.dialogLoginButton)
        val cancelBtn = view.findViewById<Button>(R.id.dialogCancelButton)


        // LAYOUT
        cancelBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.divider))
        //view.setBackgroundColor(getResources().getColor(R.color.dialog_backdrop))



        // EVENT LISTENERS
        // Handle login button click
        loginBtn.setOnClickListener {
            // Handle login action here
            findNavController().navigate(R.id.action_communityFragment_to_loginFragment)
            dismiss()
        }

        // Handle cancel button click
        cancelBtn.setOnClickListener {
            dismiss()
        }

        return view
    }

}
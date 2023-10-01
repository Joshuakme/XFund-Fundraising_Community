package com.example.xfund

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentPaymentSuccessBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentSuccessFragment : Fragment() {
    private lateinit var binding: FragmentPaymentSuccessBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_success,
            container,
            false
        )

        binding.paymentToMenuBtn.setOnClickListener {
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
            bottomNav?.visibility = View.VISIBLE
            findNavController().navigate(R.id.action_paymentSuccessFragment_to_homeFragment)
        }

        binding.shareBtnCV.setOnClickListener {
            shareDonation()
        }

        return binding.root
    }
    private fun shareDonation() {
        val textToShare = "Join me in making a positive impact and spreading happiness and change lives together! #MakingADifference #JoinMe"

        // Create an intent to send text to a messaging app
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        sendIntent.type = "text/plain"

        // Check if there are any apps that can handle this intent
        if (sendIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(Intent.createChooser(sendIntent, "Share with"))
        } else {

        }
    }
}
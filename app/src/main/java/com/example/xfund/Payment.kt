package com.example.xfund

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.databinding.FragmentPaymentBinding

class Payment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentPaymentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment,
            container,
            false
        )

        binding.donateBtn.setOnClickListener{
            findNavController().navigate(R.id.action_payment_to_paymentSuccessFragment)
        }
        binding.changeCardTxt.setOnClickListener{
            findNavController().navigate(R.id.action_payment_to_paymentMethodFragment)
        }
        binding.backBtn.setOnClickListener{
            findNavController().navigateUp()
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}
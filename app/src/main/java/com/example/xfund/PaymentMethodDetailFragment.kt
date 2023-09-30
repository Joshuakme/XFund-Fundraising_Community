package com.example.xfund

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.xfund.databinding.FragmentPaymentMethodDetailBinding

class PaymentMethodDetailFragment : Fragment() {

    private lateinit var binding: FragmentPaymentMethodDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_method_detail,
            container,
            false
        )

        // Retrieve each field from the bundle
        val cardName = arguments?.getString("cardName")
        val cardNo = arguments?.getString("cardNo")
        val cardExpiry = arguments?.getString("cardExpiry")
        val cardCvv = arguments?.getString("cardCvv")

        binding.paymentDetailnameTxt.text = cardName
        binding.paymentDetailNoTxt.text = cardNo


        return binding.root
    }
}
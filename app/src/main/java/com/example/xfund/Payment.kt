package com.example.xfund

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentPaymentBinding
import com.example.xfund.util.FirebaseHelper
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Payment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private val firestoreRepository = FirebaseHelper()

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

        val projectId = arguments?.getString("projectId")

        binding.donateBtn.setOnClickListener{
            val donationAmount = binding.donateAmountInput.text.toString().toInt()

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val isDeleted = firestoreRepository.donateToProject(projectId ?: "", donationAmount)

                if(isDeleted) {
                    Toast.makeText(context, "Donate Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Donate Rejected", Toast.LENGTH_SHORT).show()
                }
            }

            findNavController().navigate(R.id.action_payment_to_paymentSuccessFragment)
        }
        binding.changeCardTxt.setOnClickListener{
            findNavController().navigate(R.id.action_payment_to_paymentMethodFragment)
        }
        binding.backBtn.setOnClickListener{
            findNavController().navigateUp()
        }

        //Fetch data from payment
        val cardName = arguments?.getString("cardName")
        val cardNo = arguments?.getString("cardNo")

        binding.savedCardName.text = cardName
        binding.savedCardNo.text = cardNo

        // Inflate the layout for this fragment
        return binding.root
    }
}
package com.example.xfund

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.xfund.databinding.FragmentPaymentBinding
import com.example.xfund.util.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Payment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private val firestoreRepository = FirebaseHelper()

    @SuppressLint("RestrictedApi")
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
//        val projectDetailClassName = "com.example.xfund.screens.project.ProjectDetailFragment"
//        val lastBackStackEntry =  findNavController().backStack.last

        val previousFragmentDestinationId = findNavController().previousBackStackEntry?.destination?.id
        //Toast.makeText(requireContext(), previousFragmentDestinationId.toString(), Toast.LENGTH_SHORT).show()

//        Toast.makeText(context, lastBackStackEntry.toString(), Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, lastBackStackEntry?.destination?.javaClass?.simpleName.toString(), Toast.LENGTH_SHORT).show()
//        if(lastBackStackEntry?.destination?.javaClass?.simpleName == projectDetailClassName ) {
//            Toast.makeText(context, "BOLEH KAAA????", Toast.LENGTH_SHORT).show()
//        }

        if(previousFragmentDestinationId == 2131362412) {
            binding.documentIdHidden.text = projectId
        }


        binding.donateBtn.setOnClickListener{
            val donationAmount = binding.donateAmountInput.text.toString().toInt()

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val isDeleted = firestoreRepository.donateToProject(binding.documentIdHidden.text.toString(), donationAmount)

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
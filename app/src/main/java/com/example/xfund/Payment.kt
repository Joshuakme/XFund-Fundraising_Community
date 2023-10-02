package com.example.xfund

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        //Donation Amount Validation
        binding.donateAmountInput.addTextChangedListener(TextFieldValidation(binding.donateAmountInput))

        binding.donateBtn.setOnClickListener{
            if (isValidate()) {
                val donationAmount = binding.donateAmountInput.text.toString().toInt()

                //Validate payment amount
                binding.donateAmountInput.addTextChangedListener(TextFieldValidation(binding.donateAmountInput))

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

    private fun isValidate(): Boolean =
        validateDonateAmount()


    private fun validateDonateAmount(): Boolean {
        if (binding.donateAmountInput.text.toString().trim().isEmpty()) {
            binding.donateAmountLayout.error = "Required Field!"
            binding.donateAmountInput.requestFocus()
            return false
        } else if (binding.donateAmountInput.text.toString().toInt() < 5){
            binding.donateAmountLayout.error = "Donation Amount must more than RM 5"
            binding.donateAmountInput.requestFocus()
            return false
        } else{
            binding.donateAmountLayout.isErrorEnabled = false
        }
        return true
    }
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.donateAmountInput -> {
                    validateDonateAmount()
                }
            }
        }
    }
}
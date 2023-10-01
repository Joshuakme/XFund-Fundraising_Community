package com.example.xfund

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.xfund.databinding.FragmentAddPaymentMethodBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddPaymentMethodFragment : Fragment() {
    private lateinit var binding: FragmentAddPaymentMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_payment_method,
            container,
            false
        )

        // Hide Bottom Navigation
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        if (bottomNav != null) {
            bottomNav.visibility = View.INVISIBLE
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        setupListeners()

        //FireStore add data
        val db = Firebase.firestore
        val addPaymentBtn =  binding.addMethodBtn

        addPaymentBtn.setOnClickListener {
            if (isValidate()) {
                val addPaymentMethodMap = hashMapOf(
                    "cardName" to binding.cardNameInput.editText?.text?.toString(),
                    "cardNo" to binding.cardNoInput.editText?.text?.toString(),
                    "cardExpiry" to binding.cardExpiryInput.editText?.text?.toString(),
                    "cardCvv" to binding.cardCvvInput.editText?.text?.toString(),
                )

                db.collection("Payment Method Collection")
                    .add(addPaymentMethodMap)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            ContentValues.TAG,
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                        Toast.makeText(context, "payment method added", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                        Toast.makeText(context, "failed to add", Toast.LENGTH_SHORT).show()
                    }

                findNavController().navigateUp()
            }

        }


        return binding.root
    }
    private fun isValidate(): Boolean =
        validateCardName() && validateCardNo() && validateCardExpiry() && validateCardCvv()

    private fun setupListeners() {
        binding.cardNameTxt.addTextChangedListener(TextFieldValidation(binding.cardNameTxt))
        binding.cardNoTxt.addTextChangedListener(TextFieldValidation(binding.cardNoTxt))
        binding.cardExpiryTxt.addTextChangedListener(TextFieldValidation(binding.cardExpiryTxt))
        binding.cardCvvTxt.addTextChangedListener(TextFieldValidation(binding.cardCvvTxt))
    }

    private fun validateCardName(): Boolean {
        if (binding.cardNameTxt.text.toString().trim().isEmpty()) {
            binding.cardNameInput.error = "Required Field!"
            binding.cardNameTxt.requestFocus()
            return false
        } else {
            binding.cardNameInput.isErrorEnabled = false
        }
        return true
    }
    private fun validateCardNo(): Boolean {
        if (binding.cardNoTxt.text.toString().trim().isEmpty()) {
            binding.cardNoInput.error = "Required Field!"
            binding.cardNoTxt.requestFocus()
            return false
        } else if (binding.cardNoTxt.text.toString().length != 16){
            binding.cardNoInput.error = "Card number must be 16 digits"
            binding.cardNoTxt.requestFocus()
            return false
        } else {
            binding.cardNoInput.isErrorEnabled = false
        }
        return true
    }
    private fun validateCardExpiry(): Boolean {
        if (binding.cardExpiryTxt.text.toString().trim().isEmpty()) {
            binding.cardExpiryInput.error = "Required Field!"
            binding.cardExpiryTxt.requestFocus()
            return false
        } else if (binding.cardExpiryTxt.text.toString().length != 5){
            binding.cardExpiryInput.error = "Card Expiry must be 5 digits"
            binding.cardExpiryTxt.requestFocus()
            return false
        } else {
            binding.cardExpiryInput.isErrorEnabled = false
        }
        return true
    }
    private fun validateCardCvv(): Boolean {
        if (binding.cardCvvTxt.text.toString().trim().isEmpty()) {
            binding.cardCvvInput.error = "Required Field!"
            binding.cardCvvTxt.requestFocus()
            return false
        } else if (binding.cardCvvTxt.text.toString().length != 3){
            binding.cardCvvInput.error = "Card CVV must be 3 digits"
            binding.cardCvvTxt.requestFocus()
            return false
        } else {
            binding.cardCvvInput.isErrorEnabled = false
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
                R.id.cardNameTxt -> {
                    validateCardName()
                }
                R.id.cardNoTxt -> {
                    validateCardNo()
                }
                R.id.cardExpiryTxt -> {
                    if (s?.length == 2) {
                        R.id.cardExpiryTxt
                        if (start == 2 && before == 1 && !s.contains("/")) {
                            binding.cardExpiryTxt.setText(s[0].toString())
                            binding.cardExpiryTxt.setSelection(1)
                        } else {
                            binding.cardExpiryTxt.setText("$s/")
                            binding.cardExpiryTxt.setSelection(3)
                        }
                    }
                    validateCardExpiry()
                }
                R.id.cardCvvTxt -> {
                    validateCardCvv()
                }
            }
        }
    }

}
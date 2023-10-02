package com.example.xfund

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.databinding.FragmentPaymentMethodDetailBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

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
        val id = arguments?.getString("documentId")
        val cardName = arguments?.getString("cardName")
        val cardNo = arguments?.getString("cardNo")

        //declare
        val editTextCardName = binding.paymentDetailnameTxt
        val textCardNo = binding.root.findViewById<EditText>(R.id.paymentDetailNoTxt)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.paymentDetailnameTxt.addTextChangedListener(TextFieldValidation(binding.paymentDetailnameTxt))

        //set
        editTextCardName.setText(cardName)

        val hiddenCardNo = hideCardNumber(cardNo.toString())
        textCardNo.setText(hiddenCardNo)

        // Delete the document
        val db = FirebaseFirestore.getInstance()
        // Define the collection and document you want to delete
        val collectionName = "Payment Method Collection" // Replace with your collection name
        val documentId = id.toString() // Replace with the ID of the document you want to delete
        val updateBtn = binding.paymentMethodDeleteBtn
        val documentRef = db.collection(collectionName).document(documentId)

        //Delete Payment Method
        binding.paymentMethodDeleteBtn.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Payment Method Deletion")
            builder.setMessage("Do you wish to delete this payment method?")

            builder.setPositiveButton("Confirm") { dialog, _ ->
                deletePaymentMethod(documentRef)
                dialog.dismiss()
                findNavController().navigateUp()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                // Cancel deletion
                dialog.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

            //edit Payment Method
            editTextCardName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newValue = s.toString()
                    val updates = hashMapOf<String, Any>(
                        "cardName" to newValue
                    )


                    binding.paymentMethodUpdateBtn.setOnClickListener {
                        if(isValidate()) {
                            it.hideKeyboard()
                            editTextCardName.clearFocus();

                            documentRef.update(updates)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Payment Method Name change successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                }
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        return binding.root
    }

    private fun deletePaymentMethod(documentRef: DocumentReference){
        documentRef.delete()
            .addOnSuccessListener {
                // Document was successfully deleted
                Toast.makeText(requireContext(), "Payment Method successfully deleted!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Fail! $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun hideCardNumber(cardNumber: String): String {
        val visibleChars = 4 // Number of visible characters at the end of the card number
        val cardLength = cardNumber.length
        val asterisks = "*".repeat(cardLength - visibleChars)
        val visiblePart = cardNumber.takeLast(visibleChars)

        return "$asterisks$visiblePart"
    }


    private fun isValidate(): Boolean =
        validateDetailname()


    private fun validateDetailname(): Boolean {
        if (binding.paymentDetailnameTxt.text.toString().trim().isEmpty()) {
            binding.paymentDetailnameInput.error = "Required Field!"
            binding.paymentDetailnameTxt.requestFocus()
            return false
        } else{
            binding.paymentDetailnameInput.isErrorEnabled = false
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
                R.id.paymentDetailnameTxt -> {
                    validateDetailname()
                }
            }
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}
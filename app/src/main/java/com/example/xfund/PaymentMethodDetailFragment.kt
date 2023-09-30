package com.example.xfund

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val cardExpiry = arguments?.getString("cardExpiry")
        val cardCvv = arguments?.getString("cardCvv")

        //declare
        val editTextCardName = binding.root.findViewById<EditText>(R.id.paymentDetailnameTxt)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        //set
        editTextCardName.setText(cardName)
        binding.paymentDetailNoTxt.text = cardNo

        // Delete the document
        val db = FirebaseFirestore.getInstance()
        // Define the collection and document you want to delete
        val collectionName = "Payment Method Collection" // Replace with your collection name
        val documentId = id.toString() // Replace with the ID of the document you want to delete

        val documentRef = db.collection(collectionName).document(documentId)

        //Delete Payment Method
        binding.paymentMethodDeleteBtn.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Payment Method Deletion")
            builder.setMessage("Do you wish to delete this payment method?")

            builder.setPositiveButton("Confirm") { dialog, _ ->
                deletePaymentMethod(documentRef)
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                // Cancel deletion
                dialog.dismiss()
                findNavController().navigateUp()
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

                binding.paymentMethodUpdateBtn.setOnClickListener{
                    documentRef.update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Payment Method Name change successfully!", Toast.LENGTH_SHORT).show()
                            editTextCardName.isEnabled = false
                        }
                        .addOnFailureListener { e ->
                        }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int,
                                       before: Int, count: Int) {
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


}
package com.example.xfund

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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

        //FireStore add data
        val db = Firebase.firestore
        binding.addMethodBtn.setOnClickListener {
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
        }


        return binding.root
    }
}
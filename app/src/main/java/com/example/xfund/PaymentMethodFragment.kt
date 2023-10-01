package com.example.xfund

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xfund.adapter.PaymentAdapter
import com.example.xfund.databinding.FragmentPaymentMethodBinding
import com.example.xfund.model.PaymentMethod
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PaymentMethodFragment : Fragment() {
    private lateinit var binding: FragmentPaymentMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_method,
            container,
            false
        )

        binding.addPaymentCard.setOnClickListener{
            findNavController().navigate(R.id.action_paymentMethodFragment_to_addPaymentMethodFragment)
        }
        binding.addPaymentWallet.setOnClickListener{
            findNavController().navigate(R.id.action_paymentMethodFragment_to_addPaymentMethodFragment)
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }


        // Inflate the layout for this fragment
        val paymentMethodRV = binding.savedCardList

        var paymentModelArrayList: ArrayList<PaymentMethod>

        val db = Firebase.firestore

        db.collection("Payment Method Collection")
            .get()
            .addOnSuccessListener { result ->
                paymentModelArrayList = createPaymentMethods(result)

                // Adapter
                val navController = findNavController()

                val previousFragmentDestinationId = findNavController().previousBackStackEntry?.destination?.id

                val paymentAdapter = PaymentAdapter(requireContext(), paymentModelArrayList, navController, previousFragmentDestinationId)

                Toast.makeText(requireContext(), previousFragmentDestinationId.toString(), Toast.LENGTH_SHORT).show()
                paymentMethodRV.adapter = paymentAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }

        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        paymentMethodRV.layoutManager = linearLayoutManager


        return binding.root
    }

    private fun createPaymentMethods(querySnapshot: QuerySnapshot): ArrayList<PaymentMethod> {
        val paymentMethodList = mutableListOf<PaymentMethod>()

        for (document in querySnapshot.documents) {
            val cardName = document.getString("cardName") ?: ""
            val cardNo = document.getString("cardNo") ?: ""
            val cardExpiry = document.getString("cardExpiry") ?: ""
            val cardCvv = document.getString("cardCvv")?: ""
            val paymentMethod = PaymentMethod(id = document.id, cardName, cardNo, cardExpiry, cardCvv)


            paymentMethodList.add(paymentMethod)
        }

        return ArrayList(paymentMethodList)
    }

}
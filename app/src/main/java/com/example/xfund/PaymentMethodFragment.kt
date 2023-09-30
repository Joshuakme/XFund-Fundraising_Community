package com.example.xfund

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
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
            findNavController().navigate(R.id.action_paymentMethodFragment_to_payment)
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
                val paymentAdapter = PaymentAdapter(requireContext(), paymentModelArrayList) { paymentMethod ->

                    // Handle the click event here,  receive the PaymentMethod object
                    Toast.makeText(requireContext(), "Clicked on ${paymentMethod.cardName}", Toast.LENGTH_SHORT).show()

                    val bundle = bundleOf(
                        "cardName" to paymentMethod.cardName,
                        "cardNo" to paymentMethod.cardNo,
                        "cardExpiry" to paymentMethod.cardExpiry,
                        "cardCvv" to paymentMethod.cardCvv,
                        "documentId" to paymentMethod.id
                    )
                    // You can also navigate or perform other actions here using the paymentMethod data.
                    findNavController().navigate(R.id.action_paymentMethodFragment_to_paymentMethodDetailFragment, bundle)

                }

                paymentMethodRV.adapter = paymentAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }



        // Below line is for setting a layout manager for our recycler view.
        // Here, we are creating a vertical list, so we will provide orientation as vertical.
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // In the next two lines, we are setting the layout manager and adapter for our recycler view.
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
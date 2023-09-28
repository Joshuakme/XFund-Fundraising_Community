package com.example.xfund

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.adapter.PaymentAdapter
import com.example.xfund.databinding.FragmentPaymentMethodBinding
import com.example.xfund.model.PaymentMethod

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

        val addPaymentCardBtn: CardView = binding.addPaymentCard
        val addPaymentWalletBtn: CardView = binding.addPaymentWallet

        addPaymentCardBtn.setOnClickListener{
            findNavController().navigate(R.id.action_paymentMethodFragment_to_addPaymentMethodFragment)
        }
        addPaymentWalletBtn.setOnClickListener{
            findNavController().navigate(R.id.action_paymentMethodFragment_to_addPaymentMethodFragment)
        }

        // Inflate the layout for this fragment
        val paymentMethodRV = binding.savedCardList

        val paymentModelArrayList: ArrayList<PaymentMethod> = ArrayList<PaymentMethod>()

        paymentModelArrayList.add(PaymentMethod("Master Card Ecas", "1234567887654321", "08/31", "123"))
        paymentModelArrayList.add(PaymentMethod("Joshhhh Card", "9999999999999999", "09/31", "567"))

        val paymentAdapter = PaymentAdapter(requireContext(), paymentModelArrayList)

        // Below line is for setting a layout manager for our recycler view.
        // Here, we are creating a vertical list, so we will provide orientation as vertical.
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // In the next two lines, we are setting the layout manager and adapter for our recycler view.
        paymentMethodRV.layoutManager = linearLayoutManager
        paymentMethodRV.adapter = paymentAdapter

        return binding.root
    }

}
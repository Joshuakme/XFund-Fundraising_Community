package com.example.xfund

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.adapter.PaymentAdapter
import com.example.xfund.model.PaymentMethod

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentMethodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentMethodFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_method, container, false)

        val paymentMethodRV = view.findViewById<RecyclerView>(R.id.savedCardList)

        val paymentModelArrayList: ArrayList<PaymentMethod> = ArrayList<PaymentMethod>()
        paymentModelArrayList.add(PaymentMethod("Master Card Ecas", "1234567887654321"))
        paymentModelArrayList.add(PaymentMethod("Visa Card Joshua", "1234567890987654"))
        paymentModelArrayList.add(PaymentMethod("Apple Pay Kyeze", "9999999999999999"))
        paymentModelArrayList.add(PaymentMethod("Samsung Pay LoongKok", "6666666666666666"))

        val paymentAdapter = PaymentAdapter(requireContext(), paymentModelArrayList)

        // Below line is for setting a layout manager for our recycler view.
        // Here, we are creating a vertical list, so we will provide orientation as vertical.
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // In the next two lines, we are setting the layout manager and adapter for our recycler view.
        paymentMethodRV.layoutManager = linearLayoutManager
        paymentMethodRV.adapter = paymentAdapter

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentMethodFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentMethodFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
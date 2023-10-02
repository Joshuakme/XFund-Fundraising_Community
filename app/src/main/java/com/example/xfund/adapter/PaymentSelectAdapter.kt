package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.PaymentSelectFragmentDirections
import com.example.xfund.R
import com.example.xfund.model.PaymentMethod

class PaymentSelectAdapter(
    private val context: Context,
    paymentModelArrayList: ArrayList<PaymentMethod>,
    private val navController: NavController,
    private val projectId: String?,
    private val donateAmt: String?
) : RecyclerView.Adapter<PaymentSelectAdapter.ItemViewHolder>() {

    private val paymentModelArrayList: ArrayList<PaymentMethod>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentSelectAdapter.ItemViewHolder {
        // create a new view
        val paymentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_payment_method, parent, false)
        return ItemViewHolder(paymentView)
    }

    override fun onBindViewHolder(holder: PaymentSelectAdapter.ItemViewHolder, position: Int) {
        val model = paymentModelArrayList[position]
        holder.cardName.text  = model.cardName
        holder.cardNo.text  = model.cardNo
        val currentDestinationId = navController?.currentDestination?.id

        if (currentDestinationId == R.id.paymentSelectFragment){
            holder.itemView.setOnClickListener {
                val action = PaymentSelectFragmentDirections.actionPaymentSelectFragmentToPayment(
                    projectId!!,
                    donateAmt!!,
                    model.id,
                    model.cardName,
                    model.cardNo,
                    model.cardExpiry,
                    model.cardCvv
                )

                navController?.navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return paymentModelArrayList.size
    }

    class ItemViewHolder(paymentView: View) : RecyclerView.ViewHolder(paymentView) {
        val cardName: TextView = paymentView.findViewById(R.id.savedCardName)
        val cardNo: TextView = paymentView.findViewById(R.id.savedCardNo)
        val editBtn: ImageView = paymentView.findViewById(R.id.paymentMethodEditBtn)
    }

    init{
        this.paymentModelArrayList = paymentModelArrayList
    }

}
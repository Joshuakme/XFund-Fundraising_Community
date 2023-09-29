package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.model.CommunityDiscussion
import com.example.xfund.model.PaymentMethod

/**
 * Adapter for the [RecyclerView] in [PaymentMethod]. Displays [PaymentMethod] data object.
 */
class PaymentAdapter(private val context: Context, paymentModelArrayList: ArrayList<PaymentMethod>) : RecyclerView.Adapter<PaymentAdapter.ItemViewHolder>() {

    private val paymentModelArrayList: ArrayList<PaymentMethod>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAdapter.ItemViewHolder {
        // create a new view
        val paymentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_payment_method, parent, false)

        return ItemViewHolder(paymentView)
    }

    override fun onBindViewHolder(holder: PaymentAdapter.ItemViewHolder, position: Int) {
        val model = paymentModelArrayList[position]
        holder.cardName.text  = model.cardName
        holder.cardNo.text  = model.cardName
    }

    override fun getItemCount(): Int {
        return paymentModelArrayList.size
    }

    class ItemViewHolder(paymentView: View) : RecyclerView.ViewHolder(paymentView) {
        val cardName: TextView = paymentView.findViewById(R.id.savedCardName)
        val cardNo: TextView = paymentView.findViewById(R.id.savedCardNo)

    }

    init{
        this.paymentModelArrayList = paymentModelArrayList
    }

}
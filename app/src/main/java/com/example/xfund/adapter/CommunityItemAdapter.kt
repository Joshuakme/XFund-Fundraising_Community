package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.model.CommunityDiscussion

class CommunityItemAdapter(private val itemList: List<CommunityDiscussion>) : RecyclerView.Adapter<CommunityItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.community_list_item,
            parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = itemList[position]
        holder.title.text = currentItem.title
        holder.desc.text = currentItem.desc
        holder.date.text = currentItem.createdOn.day.toString() + "-" +
                            currentItem.createdOn.month.toString() + "-" +
                            currentItem.createdOn.year.toString()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        lateinit var title: TextView
        lateinit var desc : TextView
        lateinit var date: TextView

        init {
            // Define click listener for the ViewHolder's View
            title  = itemView.findViewById(R.id.community_item_title)
            desc  = itemView.findViewById(R.id.community_item_desc)
            date  = itemView.findViewById(R.id.community_item_date)
        }
    }

}
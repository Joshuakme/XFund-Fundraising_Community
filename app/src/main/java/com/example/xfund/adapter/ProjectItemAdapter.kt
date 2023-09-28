package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.model.Project


class ProjectItemAdapter(private val itemList : List<Project>) : RecyclerView.Adapter<ProjectItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.projects_list_card_cell,
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
        holder.name.text = currentItem.name
        holder.description.text = currentItem.description
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name: TextView
        val description : TextView

        init {
            // Define click listener for the ViewHolder's View
            name  = itemView.findViewById(R.id.CardTitle)
            description = itemView.findViewById(R.id.CardDesc)
        }
    }

}




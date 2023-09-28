package com.example.xfund.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.model.CommunityDiscussion
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.xfund.screens.navigation.CommunityFragmentDirections
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class CommunityItemAdapter(val context: Context, val itemList: List<CommunityDiscussion>, private val navController: NavController) : RecyclerView.Adapter<CommunityItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.community_list_item,
            parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = itemList[position]


        // Set data to the view
        holder.title.text = currentItem.title
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        holder.date.text = currentItem.createdOn.format(dateFormat)
        for (tag in currentItem.tags) {
            holder.tags.addView(Chip(context).apply {
                text = tag
                setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.md_theme_light_primary)))
                textSize = 12.0f
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                chipStartPadding = 1.0f
                isClickable = false
                chipEndPadding = 1.0f
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.md_theme_light_inversePrimary))
            })
        }
        holder.author.text = currentItem.author

        holder.discussionItem.setOnClickListener {

            // Navigate to FragmentB when item is clicked
            val action = CommunityFragmentDirections.actionCommunityFragmentToDiscussionDetailFragment (
                currentItem.title,
                currentItem.desc,
                currentItem.createdOn.format(dateFormat),
                currentItem.author,
                currentItem.tags.toTypedArray(),
            )
            navController.navigate(action)
        }


    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        lateinit var discussionItem: ConstraintLayout
        lateinit var title: TextView
        lateinit var date: TextView
        lateinit var tags: ChipGroup
        lateinit var author: TextView

        init {
            // Define click listener for the ViewHolder's View
            discussionItem = itemView.findViewById(R.id.community_discussion_item)
            title  = itemView.findViewById(R.id.community_item_title)
            date  = itemView.findViewById(R.id.community_item_date)
            tags  = itemView.findViewById(R.id.tagChipGroup)
            author = itemView.findViewById(R.id.discussion_author)
        }
    }

}
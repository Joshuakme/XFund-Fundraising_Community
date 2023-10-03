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
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.model.CommunityDiscussion
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import androidx.navigation.NavController
import com.example.xfund.screens.navigation.CommunityFragmentDirections
import com.example.xfund.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class CommunityItemAdapter(
    val context: Context,
    private val itemList: List<CommunityDiscussion>,
    private val navController: NavController
) : RecyclerView.Adapter<CommunityItemAdapter.ViewHolder>() {

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
        val currentItem = itemList[position]

        // Set data to the view
        holder.title.text = currentItem.title
        // Date
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        holder.date.text = currentItem.createdOn.format(dateFormat)
        // Clear existing chips
        holder.tags.removeAllViews()
        // Tags
        for (tag in currentItem.tags) {
            createChip(holder.tags, tag)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val authorName = FirebaseHelper().getAuthorName(currentItem.author)
            withContext(Dispatchers.Main) {
                holder.author.text = authorName
            }
        }

        holder.discussionItem.setOnClickListener {

            // Navigate to FragmentB when item is clicked
            val action = CommunityFragmentDirections.actionCommunityFragmentToDiscussionDetailFragment (
                    currentItem.title,
                    currentItem.desc,
                    currentItem.createdOn.format(dateFormat),
                    holder.author.text as String,
                    currentItem.tags.toTypedArray(),
                )

            if (action != null) {
                navController.navigate(action)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var discussionItem: ConstraintLayout
        var title: TextView
        var date: TextView
        var tags: ChipGroup
        var author: TextView

        init {
            // Define click listener for the ViewHolder's View
            discussionItem = itemView.findViewById(R.id.community_discussion_item)
            title  = itemView.findViewById(R.id.community_item_title)
            date  = itemView.findViewById(R.id.community_item_date)
            tags  = itemView.findViewById(R.id.tagChipGroup)
            author = itemView.findViewById(R.id.discussion_author)
        }
    }


    private fun createChip(tagChipGroup: ChipGroup, tagName: String) {
        tagChipGroup.addView(Chip(context).apply {
            text = tagName
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
}
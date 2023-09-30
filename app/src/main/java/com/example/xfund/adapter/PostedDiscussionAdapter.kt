package com.example.xfund.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.model.CommunityDiscussion
import com.example.xfund.screens.community.ViewPostedCommunityFragmentDirections
import com.example.xfund.util.FirebaseHelper
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class PostedDiscussionAdapter(
    val postedMapList: List<Map<String,CommunityDiscussion>>,
    private val navController: NavController): RecyclerView.Adapter<PostedDiscussionAdapter.ViewHolder>() {

    // Variables
    private val firestoreRepository = FirebaseHelper()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.view_posted_community_item,
            parent, false)
        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PostedDiscussionAdapter.ViewHolder, position: Int) {
        val currentMapItem = postedMapList[position]

        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for((discussionId, discussionItem) in currentMapItem) {
            holder.title.text = discussionItem.title
            holder.date.text = discussionItem.createdOn.format(dateFormat)

            // EVENT LISTENERS
            holder.editBtn.setOnClickListener {
                val action = ViewPostedCommunityFragmentDirections.actionViewPostedCommunityFragmentToEditDiscussionFragment(
                    discussionId,
                    discussionItem.title,
                    discussionItem.desc,
                    discussionItem.tags.toTypedArray(),
                    discussionItem.createdOn.format(dateFormat)
                )

                navController?.navigate(action)
            }

            holder.postedItemCard.setOnClickListener {
                // Use a coroutine to fetch the display name asynchronously
                CoroutineScope(Dispatchers.Main).launch {
                    val displayName = firestoreRepository.fetchUserDetails(discussionItem.author)?.displayName ?: ""

                    val action = ViewPostedCommunityFragmentDirections.actionViewPostedCommunityFragmentToDiscussionDetailFragment(
                        discussionItem.title,
                        discussionItem.desc,
                        discussionItem.createdOn.format(dateFormat),
                        displayName,
                        discussionItem.tags.toTypedArray(),
                    )

                    navController?.navigate(action)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return postedMapList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var postedItemCard: MaterialCardView
        var title: TextView
        var date: TextView
        var editBtn: Button
        lateinit var authorName: String

        init {
            // Define click listener for the ViewHolder's View
            postedItemCard = itemView.findViewById(R.id.postedItemCard)
            title  = itemView.findViewById(R.id.postedDiscussionTitle)
            date  = itemView.findViewById(R.id.postedDiscussionDate)
            editBtn = itemView.findViewById(R.id.postedDiscussionEditButton)
        }
    }
}
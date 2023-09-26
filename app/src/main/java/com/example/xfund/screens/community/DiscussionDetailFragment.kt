package com.example.xfund.screens.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.xfund.R
import com.example.xfund.model.CommunityDiscussion
import java.text.SimpleDateFormat


class DiscussionDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discussion_detail, container, false)
    }

    fun displayDiscussionDetails(communityDiscussion: CommunityDiscussion) {
        val authorTextView = view?.findViewById<TextView>(R.id.discussionAuthor)
        val titleTextView = view?.findViewById<TextView>(R.id.discussionTitle)
        val descTextView = view?.findViewById<TextView>(R.id.discussionDescription)
        val tagsTextView = view?.findViewById<TextView>(R.id.detailTagChipGroup)
        val createdOnTextView = view?.findViewById<TextView>(R.id.discussionDate)

        authorTextView?.text = communityDiscussion.author
        titleTextView?.text = communityDiscussion.title
        descTextView?.text = communityDiscussion.desc
        tagsTextView?.text = communityDiscussion.tags.joinToString(", ")
        createdOnTextView?.text = SimpleDateFormat("yyyy-MM-dd").format(communityDiscussion.createdOn)
    }
}
package com.example.xfund.screens.community

import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentDiscussionDetailBinding
import com.example.xfund.model.CommunityDiscussion
import java.text.SimpleDateFormat
import java.util.Date


class DiscussionDetailFragment : Fragment() {
    private lateinit var binding: FragmentDiscussionDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discussion_detail, container, false)

        // Variable Declaration
        val txtTitle = binding.discussionTitle
        val txtDesc = binding.discussionDescription
        val tagsChipGroup = binding.detailTagChipGroup
        val txtAuthor = binding.discussionAuthor
        val txtDate = binding.discussionDate
        val backBtn = binding.backButton


        // Set Values
        txtTitle.text = arguments?.getString("title")
        txtDesc.text = arguments?.getString("desc")
        val tagList = arguments?.getStringArrayList("tags")
        txtAuthor.text = arguments?.getString("author")
        txtDate.text = arguments?.getString("date")


        // EVENT LISTENERS
        // Back from detail discussion screen to community list Screen
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_discussionDetailFragment_to_communityFragment)
        }


        return binding.root
    }


    companion object {
        fun newInstance(title: String, desc: String, tags: List<String>, author: String, date: Date): DiscussionDetailFragment {
            val fragment = DiscussionDetailFragment()
            val args = Bundle()
            args.putString("TITLE", title)
            args.putString("DESC", desc)
            args.putStringArrayList("TAGS", ArrayList(tags))
            args.putString("AUTHOR", author)
            args.putString("DATE", SimpleDateFormat("yyyy-MM-dd").format(date))

            fragment.arguments = args
            return fragment
        }
    }
}
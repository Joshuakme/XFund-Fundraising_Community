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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Variable Declaration
        val txtTitle = binding.discussionTitle
        val txtDesc = binding.discussionDescription
        val txtAuthor = binding.discussionAuthor
        val txtDate = binding.discussionDate
        val backBtn = binding.backButton


        // Set Values
        txtTitle.text = arguments?.getString("title")
        txtDesc.text = arguments?.getString("desc")
        txtAuthor.text = arguments?.getString("author")
        txtDate.text = arguments?.getString("date")


        // EVENT LISTENERS
        // Back from detail discussion screen to community list Screen
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_discussionDetailFragment_to_communityFragment)
        }
    }
}
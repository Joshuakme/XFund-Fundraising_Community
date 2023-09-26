package com.example.xfund.screens.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.CommunityItemAdapter
import com.example.xfund.adapter.ImageSliderAdapter
import com.example.xfund.model.CommunityDiscussion
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Date

class CommunityFragment : Fragment() {
    // private var _binding: CommunityFragmentBinding ? = null
    private lateinit var addDiscussionButton : LinearLayout
    private lateinit var discussionList: List<CommunityDiscussion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)


        // Variable
        val communityRecycler: RecyclerView = view.findViewById(R.id.CommunityRecycleView)
        discussionList = listOf(
            CommunityDiscussion("author1", "title1", "desc1", listOf("babi", "ayam"),
                Date(2023, 12, 31, 10, 55, 33)),
            CommunityDiscussion("author2", "title2", "desc question 2", listOf("superman", "sinchan"),
                Date(2023, 1, 2, 15, 55, 33))
        )

        // RecyclerView
        val adapter = CommunityItemAdapter(discussionList)
        communityRecycler.adapter = adapter

        // view.findViewById<TextView>(R.id.textView).text = android.os.Build.VERSION.SDK_INT.toString()
        addDiscussionButton = view.findViewById(R.id.AddButtonLinearLayout)

        addDiscussionButton.setOnClickListener { view: View ->
            findNavController().navigate(R.id.action_communityFragment_to_addDiscussionFragment)
        }

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)

        if (bottomNav != null) {
            bottomNav.visibility = View.VISIBLE
        }

        return view
    }
}
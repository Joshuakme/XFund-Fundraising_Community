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
            CommunityDiscussion("Emily Johnson",
                "Crowdfunding Campaign for Local School Renovation",
                "Let's discuss strategies and ideas for launching a crowdfunding campaign to raise funds for renovating our local school. We need your input on how to reach our fundraising goal!",
                listOf("Education", "Crowdfunding", "Renovation", "Community", "School"),
                Date(2023, 7, 12, 14, 43, 23)),
            CommunityDiscussion("David Anderson", "Nonprofit Gala Event Planning",
                "Join the discussion on planning a nonprofit gala event to raise funds for a local charity. Share your expertise on event logistics, fundraising ideas, and securing sponsorships.",
                listOf("Nonprofit", "Gala", "Fundraising Event", "Charity", "Sponsorships"),
                Date(2023, 5, 2, 8, 5, 33)),
            CommunityDiscussion("Sarah Williams", "Online Fundraising Platforms Comparison",
                "Let's compare different online fundraising platforms like GoFundMe, Kickstarter, and Indiegogo. Share your experiences and recommendations for various fundraising campaigns.",
                listOf("Fundraising Platforms", "Online Fundraising", "Crowdfunding", "Comparison", "Recommendations"),
                Date(2023, 1, 27, 23, 0, 33))
        )

        // RecyclerView
        val adapter = CommunityItemAdapter(requireContext(), discussionList, parentFragmentManager)
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
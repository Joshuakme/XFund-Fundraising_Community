package com.example.xfund.screens.navigation

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.CommunityItemAdapter
import com.example.xfund.model.CommunityDiscussion
import com.example.xfund.util.FirebaseHelper
import com.example.xfund.util.LoginDialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class CommunityFragment : Fragment() {
    private val firestoreRepository = FirebaseHelper()
    private lateinit var addDiscussionButton : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ELEMENTS
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        addDiscussionButton = view.findViewById(R.id.AddButtonLinearLayout)
        val communityRecycler: RecyclerView = view.findViewById(R.id.CommunityRecycleView)

        // LAYOUT SETTINGS
        bottomNav?.visibility = View.VISIBLE


        // EVENT LISTENERS
        addDiscussionButton.setOnClickListener { view: View ->
            if(isLogin()) {
                findNavController().navigate(R.id.action_communityFragment_to_addDiscussionFragment)
            }
            else {
                val dialogFragment = LoginDialogFragment()
                dialogFragment.show(parentFragmentManager, "LoginDialog")
            }
        }

        // Use a coroutine scope to get all discussions
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val discussionList = firestoreRepository.getAllDiscussions()

            if (discussionList.isNotEmpty()) {
                // RecyclerView
                val navController = NavHostFragment.findNavController(view.findFragment())
                val adapter = CommunityItemAdapter(requireContext(), discussionList, navController)
                communityRecycler.adapter = adapter
            } else {
                Toast.makeText(context, "Failed to load discussions", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createCommunityDiscussions(querySnapshot: QuerySnapshot): List<CommunityDiscussion> {
        val communityDiscussions = mutableListOf<CommunityDiscussion>()

        for (document in querySnapshot.documents) {
            val author = document.getString("author") ?: ""
            val title = document.getString("title") ?: ""
            val desc = document.getString("desc") ?: ""
            val tags = document.get("tags") as? ArrayList<String> ?: emptyList()
            val timestamp = document.getTimestamp("createdOn")

            val createdOn = if (timestamp != null) {
                timestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
            } else {
                LocalDateTime.now()
            }

            val communityDiscussion = CommunityDiscussion(author, title, desc, tags, createdOn)
            communityDiscussions.add(communityDiscussion)
        }

        return communityDiscussions.toList()
    }

    private fun isLogin(): Boolean {
        var sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val user = FirebaseAuth.getInstance().currentUser

        // Check if logged in
        return sharedPreferences?.getBoolean("IsLogin", false) == true && user != null
    }
}



// Initializing list for Community RecyclerView
//        discussionList = listOf(
//            CommunityDiscussion("Emily Johnson",
//                "Crowdfunding Campaign for Local School Renovation",
//                "Let's discuss strategies and ideas for launching a crowdfunding campaign to raise funds for renovating our local school. We need your input on how to reach our fundraising goal!",
//                listOf("Education", "Crowdfunding", "Renovation", "Community", "School"),
//                LocalDateTime.of(2022, 7, 12, 14, 43, 23)),
//            CommunityDiscussion("David Anderson", "Nonprofit Gala Event Planning",
//                "Join the discussion on planning a nonprofit gala event to raise funds for a local charity. Share your expertise on event logistics, fundraising ideas, and securing sponsorships.",
//                listOf("Nonprofit", "Gala", "Fundraising Event", "Charity", "Sponsorships"),
//                LocalDateTime.of(2021, 5, 2, 8, 5, 33)),
//            CommunityDiscussion("Sarah Williams", "Online Fundraising Platforms Comparison",
//                "Let's compare different online fundraising platforms like GoFundMe, Kickstarter, and Indiegogo. Share your experiences and recommendations for various fundraising campaigns.",
//                listOf("Fundraising Platforms", "Online Fundraising", "Crowdfunding", "Comparison", "Recommendations"),
//                LocalDateTime.of(2023, 1, 27, 23, 0, 33))
//        )
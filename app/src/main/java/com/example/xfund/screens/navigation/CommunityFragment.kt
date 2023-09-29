package com.example.xfund.screens.navigation

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.CommunityItemAdapter
import com.example.xfund.model.CommunityDiscussion
import com.example.xfund.util.FirebaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.reflect.typeOf

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        // Variable
        val communityRecycler: RecyclerView = view.findViewById(R.id.CommunityRecycleView)
        val firebaseHelper = FirebaseHelper()

        // Fetch data from Firebase
        val db = Firebase.firestore     // Firestore

        db.collection("discussions")
            .orderBy("createdOn", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                discussionList = createCommunityDiscussions(it)

                // RecyclerView
                val navController = NavHostFragment.findNavController(this)
                val adapter = CommunityItemAdapter(requireContext(), discussionList, navController)
                communityRecycler.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }

        // Event Listeners
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun createCommunityDiscussions(querySnapshot: QuerySnapshot): List<CommunityDiscussion> {
        val communityDiscussions = mutableListOf<CommunityDiscussion>()

        for (document in querySnapshot.documents) {
            val author = document.getString("author") ?: ""
            val title = document.getString("title") ?: ""
            val desc = document.getString("desc") ?: ""
            val tags = document.get("tags") as? List<String> ?: emptyList()
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
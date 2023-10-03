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
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.google.android.material.card.MaterialCardView
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
        val loadingSpinner = view.findViewById<ConstraintLayout>(R.id.communityLoadingSpinnerConstraintLayout)
        val communityErrorCard = view.findViewById<MaterialCardView>(R.id.communityErrorCard)
        val user = FirebaseAuth.getInstance().currentUser

        // LAYOUT SETTINGS
        bottomNav?.visibility = View.VISIBLE


        // EVENT LISTENERS
        addDiscussionButton.setOnClickListener { view: View ->
            if(user != null) {
                findNavController().navigate(R.id.action_communityFragment_to_addDiscussionFragment)
            }
            else {
                val dialogFragment = LoginDialogFragment()
                dialogFragment.show(parentFragmentManager, "LoginDialog")
            }
        }

        // Use a coroutine scope to get all discussions
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            // Show loading spinner while fetching data
            loadingSpinner.visibility = View.VISIBLE
            communityRecycler.visibility = View.GONE
            communityErrorCard.visibility = View.GONE

            val discussionList = firestoreRepository.getAllDiscussions()

            if (discussionList.isNotEmpty()) {
                // Hide loading spinner and show RecyclerView with data
                loadingSpinner.visibility = View.GONE
                communityRecycler.visibility = View.VISIBLE

                val navController = NavHostFragment.findNavController(view.findFragment())
                val adapter = CommunityItemAdapter(requireContext(), discussionList, navController)
                communityRecycler.adapter = adapter
            } else {
                // Hide loading spinner and show error message
                loadingSpinner.visibility = View.GONE
                communityErrorCard.visibility = View.VISIBLE
            }
        }
    }
}
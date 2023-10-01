package com.example.xfund.screens.community

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.PostedDiscussionAdapter
import com.example.xfund.util.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewPostedCommunityFragment : Fragment() {
    private val firestoreRepository = FirebaseHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_posted_community, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // VIEW ELEMENTS
        val postedCommunitybackButton = view.findViewById<ImageButton>(R.id.postedCommunitybackButton)
        val postedCommunityRecycleView = view.findViewById<RecyclerView>(R.id.postedCommunityRecycleView)


        // Use a coroutine scope to fetch user details
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val postedDiscussionMapList = firestoreRepository.fetchPostedDiscussions()
            val navController = NavHostFragment.findNavController(view.findFragment())

            val adapter = PostedDiscussionAdapter(postedDiscussionMapList, navController)
            postedCommunityRecycleView.adapter = adapter
            postedCommunityRecycleView.layoutManager = LinearLayoutManager(context)
        }


        // EVENT LISTENERS
        postedCommunitybackButton.setOnClickListener {
            findNavController().navigate(R.id.action_viewPostedCommunityFragment_to_profileFragment)
        }
    }



}
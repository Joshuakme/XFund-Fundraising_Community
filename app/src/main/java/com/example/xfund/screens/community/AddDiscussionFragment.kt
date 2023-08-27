package com.example.xfund.screens.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddDiscussionFragment : Fragment() {
    private lateinit var backButton : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_discussion, container, false)
        backButton = view.findViewById(R.id.backButton)


        // Event Listeners
        backButton.setOnClickListener {view: View ->
            findNavController().navigate(R.id.action_addDiscussionFragment_to_communityFragment)
        }

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)

        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }

        return view
    }
}
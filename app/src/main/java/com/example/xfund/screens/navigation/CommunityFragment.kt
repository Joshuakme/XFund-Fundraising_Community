package com.example.xfund.screens.navigation

import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.CommunityItemAdapter
import com.example.xfund.data.CommunityDatasource
import com.google.android.material.bottomnavigation.BottomNavigationView

class CommunityFragment : Fragment() {
    // private var _binding: CommunityFragmentBinding ? = null
    private lateinit var addDiscussionButton : LinearLayout

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
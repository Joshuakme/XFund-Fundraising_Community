package com.example.xfund.screens.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.ProjectItemAdapter
import com.example.xfund.databinding.FragmentProjectsBinding
import com.example.xfund.model.Project
import com.google.firebase.database.DatabaseReference
import java.util.Date





class ProjectsFragment : Fragment(){
    private lateinit var binding: FragmentProjectsBinding
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_projects, container, false
        )


        val recyclerView: RecyclerView = binding.ProjectRecycleView

        val itemList = listOf(
            Project(
                R.drawable.baseline_account_circle,
                "hello world",
                Date(2028, 2, 11),
                Date(2029, 2, 11),
                10000,
                1000,
                "Hellow"
            ),
            Project(
                R.drawable.baseline_favorite_24,
                "no hello",
                Date(2028, 2, 11),
                Date(2029, 2, 11),
                10000,
                5000,
                "Hellow"
            )
        )
        val navController = NavHostFragment.findNavController(this)
        val adapter = ProjectItemAdapter(requireContext(), itemList, navController)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

      

        // Search Elements
        val searchBar: androidx.appcompat.widget.SearchView = binding.ProjectSearch
        val txtSearch = searchBar.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        val searchIcon: ImageView = searchBar.findViewById(androidx.appcompat.R.id.search_mag_icon)

        searchIcon.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.gray_300),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        txtSearch.setHint(R.string.search_query_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_300))


        /*        binding.ProjectRecycleView.setOnClickListener{
            findNavController().navigate(R.id.action_projectsFragment_to_projectDetailFragment)
        }*/


        return binding.root
    }

}
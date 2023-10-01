package com.example.xfund.screens.navigation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.ProjectItemAdapter
import com.example.xfund.databinding.FragmentProjectsBinding
import com.example.xfund.model.Project
import com.example.xfund.util.FirebaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


class ProjectsFragment : Fragment(){
    private lateinit var binding: FragmentProjectsBinding
    private var projectList: List<Project> = emptyList()
    private lateinit var recyclerView: RecyclerView

    private val firestoreRepository = FirebaseHelper()

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
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_projects, container, false
        )


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //declare variables
        recyclerView = binding.projectRecycleView

        // Use a coroutine scope to get all discussions
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            projectList = firestoreRepository.getAllProjects()

            if (projectList.isNotEmpty()) {
                // RecyclerView
                val navController = NavHostFragment.findNavController(view.findFragment())
                val adapter = ProjectItemAdapter(requireContext(), projectList, navController)

                recyclerView.adapter = adapter

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            } else {
                Toast.makeText(context, "Failed to load discussions", Toast.LENGTH_SHORT).show()
            }
        }

        // ELEMENTS IN FRAGMENT
        // Search Elements
        val searchBar : SearchView = binding.SearchBar
        val txtSearch = searchBar.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        val searchIcon: ImageView = searchBar.findViewById(androidx.appcompat.R.id.search_mag_icon)
        // Search Bar
        searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_300),android.graphics.PorterDuff.Mode.SRC_IN)
        txtSearch.setHint(R.string.search_query_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_300))

        searchBar.clearFocus()
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterListSubmit(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        // Show bottom nav when load this page
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.visibility = View.VISIBLE

    }

    private fun filterList(query: String?) {
        if(query != null) {
            val filteredList = arrayListOf<Project>()
            for (i in projectList) {
                if (i.name.toLowerCase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                val navController = NavHostFragment.findNavController(this)
                val adapter = ProjectItemAdapter(requireContext(), filteredList, navController)
                recyclerView.adapter = adapter
            } else {
                val navController = NavHostFragment.findNavController(this)
                val adapter = ProjectItemAdapter(requireContext(), filteredList, navController)
                recyclerView.adapter = adapter
            }
        }
    }

    private fun filterListSubmit(query: String?) {
        if(query != null) {
            val filteredList = arrayListOf<Project>()
            for (i in projectList) {
                if (i.name.toLowerCase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                val navController = NavHostFragment.findNavController(this)
                val adapter = ProjectItemAdapter(requireContext(), filteredList, navController)
                recyclerView.adapter = adapter
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
            } else {
                val navController = NavHostFragment.findNavController(this)
                val adapter = ProjectItemAdapter(requireContext(), filteredList, navController)
                recyclerView.adapter = adapter
            }
        }
    }
}
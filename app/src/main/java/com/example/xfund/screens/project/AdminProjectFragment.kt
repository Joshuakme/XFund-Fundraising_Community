package com.example.xfund.screens.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.AdminProjectAdapter
import com.example.xfund.adapter.ProjectItemAdapter
import com.example.xfund.databinding.FragmentAdminProjectBinding
import com.example.xfund.model.Project
import com.example.xfund.util.FirebaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class AdminProjectFragment : Fragment()
{
    private val firestoreRepository = FirebaseHelper()
    private lateinit var binding : FragmentAdminProjectBinding
    private var projectAdminList : List<Project> = emptyList()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_admin_project, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.projectRecycleView

        // Use a coroutine scope to get all projects
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            projectAdminList = firestoreRepository.getAllProjects()
            if (projectAdminList.isNotEmpty()) {

                val adapter = AdminProjectAdapter(requireContext(), projectAdminList,
                    object : AdminProjectAdapter.OnEditButtonClickListener {
                        override fun onEditClick(position: Int, project: Project) {
                            // Handle edit click event
                            val action = AdminProjectFragmentDirections.actionAdminProjectFragmentToEditProjectFragment(project)
                            findNavController().navigate(action)
                        }
                    })
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
        searchBar.setQuery("", false)
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

        binding.backButton.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.projectAddButton.setOnClickListener{
            findNavController().navigate(R.id.action_adminProjectFragment_to_addProjectFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        // Clear the search query when the fragment is resumed
        binding.SearchBar.setQuery("", false)
    }

    private fun filterList(query: String?) {
        if(query != null) {
            val filteredList = arrayListOf<Project>()
            for (i in projectAdminList) {
                if (i.name.toLowerCase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {

            } else {
                val adapter = AdminProjectAdapter(requireContext(), filteredList,
                    object : AdminProjectAdapter.OnEditButtonClickListener {
                        override fun onEditClick(position: Int, project: Project) {
                            // Handle edit click event
                            val action = AdminProjectFragmentDirections.actionAdminProjectFragmentToEditProjectFragment(project)
                            findNavController().navigate(action)
                        }
                    })
                recyclerView.adapter = adapter
            }
        }
    }

    private fun filterListSubmit(query: String?) {
        if(query != null) {
            val filteredList = arrayListOf<Project>()
            for (i in projectAdminList) {
                if (i.name.toLowerCase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
            } else {
                val adapter = AdminProjectAdapter(requireContext(), filteredList,
                    object : AdminProjectAdapter.OnEditButtonClickListener {
                    override fun onEditClick(position: Int, project: Project) {
                        // Handle edit click event
                        val action = AdminProjectFragmentDirections.actionAdminProjectFragmentToEditProjectFragment(project)
                        findNavController().navigate(action)
                    }
                })
                recyclerView.adapter = adapter
            }
        }
    }
}
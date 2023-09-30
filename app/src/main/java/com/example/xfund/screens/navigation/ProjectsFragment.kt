package com.example.xfund.screens.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.ProjectItemAdapter
import com.example.xfund.databinding.FragmentProjectsBinding
import com.example.xfund.model.Project
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date


class ProjectsFragment : Fragment(){
    private lateinit var binding: FragmentProjectsBinding
    private lateinit var projectList: List<Project>
    private lateinit var recyclerView: RecyclerView

    // Fetch data from Firebase
    private val db = Firebase.firestore

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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_projects, container, false
        )

        //declare variables
        recyclerView = binding.projectRecycleView


        db.collection("projects")
            .orderBy("start_date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener {
                projectList = createProject(it)

                // RecyclerView
                val navController = NavHostFragment.findNavController(this)
                val adapter = ProjectItemAdapter(requireContext(), projectList, navController)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // ELEMENTS IN FRAGMENT
        // Search Elements
        val searchBar : SearchView = binding.SearchBar
        val txtSearch = searchBar.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        val searchIcon: ImageView = searchBar.findViewById(androidx.appcompat.R.id.search_mag_icon)
        // Search Bar
        searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_300),android.graphics.PorterDuff.Mode.SRC_IN)
        txtSearch.setHint(R.string.search_query_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_300))

        // Show bottom nav when load this page
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.visibility = View.VISIBLE

        return binding.root
    }

    private fun createProject(querySnapshot: QuerySnapshot): List<Project> {
        val projects = mutableListOf<Project>()

        for (document in querySnapshot.documents) {
            val cover = document.getString("cover") ?: ""
            val name = document.getString("name") ?: ""
            val description = document.getString("description") ?: ""
            val startLocalDateTime = document.getDate("start_date")
            val endLocalDateTime = document.getDate("end_date")
            val fund_collected = document.getDouble("fund_collected") ?: 0.0
            val fund_target = document.getDouble("fund_target") ?: 0.0

            // Convert Date objects to LocalDateTime using Calendar
            val startCalendar = Calendar.getInstance()
            startCalendar.time = startLocalDateTime ?: Date(0) // Use some default date if start_date is null
            val start_date = LocalDateTime.of(
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH) + 1,
                startCalendar.get(Calendar.DAY_OF_MONTH),
                startCalendar.get(Calendar.HOUR_OF_DAY),
                startCalendar.get(Calendar.MINUTE)
            )

            val endCalendar = Calendar.getInstance()
            endCalendar.time = endLocalDateTime ?: Date(0) // Use some default date if end_date is null
            val end_date = LocalDateTime.of(
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH) + 1,
                endCalendar.get(Calendar.DAY_OF_MONTH),
                endCalendar.get(Calendar.HOUR_OF_DAY),
                endCalendar.get(Calendar.MINUTE)
            )

            val project = Project(cover, name, description, start_date, end_date, fund_collected, fund_target)
            projects.add(project)
        }

        return projects.toList()
    }


}
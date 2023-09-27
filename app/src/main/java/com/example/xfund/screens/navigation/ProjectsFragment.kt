package com.example.xfund.screens.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.ProjectItemAdapter
import com.example.xfund.databinding.FragmentProjectsBinding
//import com.example.xfund.model.PROJECT_NAME_EXTRA
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_projects,container,false)

        var recyclerView : RecyclerView = binding.ProjectRecycleView

        val itemList = listOf(
            Project(R.drawable.baseline_account_circle,"hello world", Date(2028,2,11), Date(2029,2,11), 10000, 1000, "Hellow" ),
            Project(R.drawable.baseline_favorite_24,"no hello", Date(2028,2,11), Date(2029,2,11), 10000, 5000, "Hellow" ))
        val adapter = ProjectItemAdapter(itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val searchView = binding.ProjectSearch



        // Set a listener to handle search queries
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission here
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text changes here
                return true
            }
        })

/*        binding.ProjectRecycleView.setOnClickListener{
            findNavController().navigate(R.id.action_projectsFragment_to_projectDetailFragment)
        }*/



        return binding.root
    }

    /*override fun onClick(project: Project) {
        val intent = Intent(requireContext(), ProjectDetailFragment::class.java)
        intent.putExtra(PROJECT_NAME_EXTRA, project.name)
        startActivity(intent)
    }*/


}
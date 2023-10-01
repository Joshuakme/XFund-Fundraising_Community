package com.example.xfund.screens.project

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.adapter.AdminProjectAdapter
import com.example.xfund.databinding.FragmentAdminProjectBinding
import com.example.xfund.model.Project
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

class AdminProjectFragment : Fragment(),
    AdminProjectAdapter.OnEditButtonClickListener,
    AdminProjectAdapter.OnDeleteButtonClickListener
{
    private lateinit var binding : FragmentAdminProjectBinding
    private lateinit var projectAdminList : List<Project>

    // Fetch data from Firebase
    private val db = Firebase.firestore.collection("projects")

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

        val recyclerView: RecyclerView = binding.projectRecycleView

        db.orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener {
                projectAdminList = createProject(it)

                // RecyclerView
                val adapter = AdminProjectAdapter(requireContext(), projectAdminList, this, this)
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

        binding.projectAddButton.setOnClickListener{
            findNavController().navigate(R.id.action_adminProjectFragment_to_addProjectFragment)
        }

        return binding.root
    }

    private fun createProject(querySnapshot: QuerySnapshot): List<Project> {
        val adminProjects = mutableListOf<Project>()

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

            val adminProject = Project(cover, name, description, start_date, end_date, fund_collected, fund_target)
            adminProjects.add(adminProject)
        }

        return adminProjects.toList()
    }

    override fun onEditClick(position: Int, project: Project) {
        val action = AdminProjectFragmentDirections.actionAdminProjectFragmentToEditProjectFragment(project)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(position: Int, project: Project) {
        val fragmentManager = requireActivity().supportFragmentManager
        val dialog = DeleteButtonDialog()
        val bundle = Bundle()
        bundle.putParcelable("project", project)
        dialog.arguments = bundle
        dialog.show(fragmentManager, "DELETE_DIALOG")
    }

    class DeleteButtonDialog: DialogFragment() {
        private val db2 = Firebase.firestore.collection("projects")

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {

                val project = arguments?.getParcelable<Project>("project")

                // Use the Builder class for convenient dialog construction.
                val builder = AlertDialog.Builder(it)
                builder.setMessage("Do you want to delete this item?")
                    .setPositiveButton("Delete") { dialog, id ->
                        deleteProject(project)
                        Toast.makeText(context,"Project Deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, id ->
                        Toast.makeText(context, "Deletion has been canceled", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                // Create the AlertDialog object and return it.
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }

        private fun deleteProject(oldProject: Project?) = CoroutineScope(
            Dispatchers.IO).launch {
            val projectQuery = db2
                .whereEqualTo("cover", oldProject?.cover)
                .whereEqualTo("name", oldProject?.name)
                .whereEqualTo("description", oldProject?.description)
                .whereEqualTo("fund_target", oldProject?.fund_target)
                .get()
                .await()
            if(projectQuery.documents.isNotEmpty()) {
                for(document in projectQuery) {
                    try {
                        db2.document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("Edit Successful", "DocumentSnapshot successfully updated!")
                            }
                            .addOnFailureListener {
                                    e -> Log.w("Edit Failed", "Error updating document", e)
                            }
                    } catch(e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "No project matched the query.", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

}
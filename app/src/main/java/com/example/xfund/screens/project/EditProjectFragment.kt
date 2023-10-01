package com.example.xfund.screens.project

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentEditProjectBinding
import com.example.xfund.model.Project
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class EditProjectFragment : Fragment() {
    private lateinit var binding: FragmentEditProjectBinding
    private lateinit var updateButton: MaterialButton
    private lateinit var projectNameText: TextInputEditText
    private lateinit var projectDescText: TextInputEditText

    private var isNameValid: Boolean = true
    private var isDescValid: Boolean = true

    // Connect to firestore projects collection
    private val db = Firebase.firestore.collection("projects")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
        R.layout.fragment_edit_project, container, false)


        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //declare variables
        updateButton = binding.EditProjectUpdateButton
        projectNameText = binding.projectNameText
        projectDescText = binding.projectDescriptionText


        //set update button disabled as default
        setBtnDisabled()

        // Retrieve the project object from arguments
        val project = arguments?.getParcelable<Project>("project")

        // Access the properties of the Project object
        val name = project?.name
        val description = project?.description

        //Put the current project items into the TextInputEditText
        projectNameText.text = Editable.Factory.getInstance().newEditable(name)
        projectDescText.text = Editable.Factory.getInstance().newEditable(description)


        // Event Listeners
        projectNameText.addTextChangedListener {
            val isNotEmpty = projectNameText.text?.isNotEmpty() == true //Check if there is at least 1 character
            val isNotBlank = projectNameText.text?.isNotBlank() == true //Check if there is something inside except for empty spaces (space, tab)
            val isLengthValid = projectNameText.length() >= 15

            isNameValid = isNotEmpty && isNotBlank && isLengthValid

            if(isFormInputValid()) {
                setBtnEnabled()
            } else {
                setBtnDisabled()
            }
        }
        projectDescText.addTextChangedListener {
            val isNotEmpty = projectDescText.text?.isNotEmpty() == true //Check if there is at least 1 character
            val isNotBlank = projectDescText.text?.isNotBlank() == true //Check if there is something inside except for empty spaces (space, tab)
            val isLengthValid = projectDescText.length() >= 50

            isDescValid = isNotEmpty && isNotBlank && isLengthValid

            if(isFormInputValid()) {
                setBtnEnabled()
            } else {
                setBtnDisabled()
            }
        }


        // Hide bottom nav when load this page
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }


        //Navigation
        //Back Button
        binding.backButton.setOnClickListener{
            findNavController().navigateUp()
        }

        //Delete button
        binding.projectAdminDeleteButtonImage.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val dialog = DeleteButtonDialog()
            val bundle = Bundle()
            bundle.putParcelable("project", project)
            dialog.arguments = bundle
            dialog.show(fragmentManager, "DELETE_DIALOG")
            findNavController().navigate(R.id.action_editProjectFragment_to_adminProjectFragment)
        }

        //Cancel button
        binding.EditProjectCancelButton.setOnClickListener{
            Toast.makeText(context, "Edit has been cancelled", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }

        //Edit button
        binding.EditProjectUpdateButton.setOnClickListener{
            val editedProjectName = projectNameText.text.toString()
            val editedProjectDesc = projectDescText.text.toString()
            val edited = hashMapOf<String, Any>(
                "name" to editedProjectName,
                "description" to editedProjectDesc
            )

            editProject(project, edited)
            Toast.makeText(context, "Project has been edited.", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

    }

    class DeleteButtonDialog: DialogFragment() {
        private val db2 = Firebase.firestore.collection("projects")

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {

                val project = arguments?.getParcelable<Project>("project")

                // Use the Builder class for convenient dialog construction.
                val builder = AlertDialog.Builder(it)
                builder.setMessage("Do you want to delete this item?")
                    .setPositiveButton("Delete") { dialog, _ ->
                        deleteProject(project)
                        Toast.makeText(context,"Project Deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
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
                                Toast.makeText(context, "Project deleted.", Toast.LENGTH_SHORT).show()
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

    private fun editProject(oldProject: Project?, newProject: HashMap<String, Any>) = CoroutineScope(Dispatchers.IO).launch {
        val projectQuery = db
            .whereEqualTo("name", oldProject?.name)
            .whereEqualTo("description", oldProject?.description)
            .whereEqualTo("fund_target", oldProject?.fund_target)
            .get()
            .await()

        if(projectQuery.documents.isNotEmpty()) {
            for(document in projectQuery) {
                try {
                    db.document(document.id)
                        .update(newProject)
                        .addOnSuccessListener {
                            Log.d("Edit Successful", "DocumentSnapshot successfully updated!")
                        }
                        .addOnFailureListener {
                                e -> Log.w("Edit Failed", "Error updating document", e)
                        }

                } catch(e: Exception) {
                    e.message
                }
            }
        }
    }

    private fun isFormInputValid(): Boolean {
        return (isNameValid && isDescValid)
    }

    private fun setBtnDisabled() {
        updateButton.isEnabled = false
        updateButton.setBackgroundColor(resources.getColor(R.color.button_disabled_bg))
    }

    private fun setBtnEnabled() {
        updateButton.isEnabled = true
        updateButton.setBackgroundColor(resources.getColor(R.color.accent_primary_300))
    }



}
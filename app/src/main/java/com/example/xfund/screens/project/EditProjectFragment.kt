package com.example.xfund.screens.project

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
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentEditProjectBinding
import com.example.xfund.model.Project
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
        R.layout.fragment_edit_project, container, false)

        //declare variables
        updateButton = binding.EditProjectUpdateButton
        projectNameText = binding.projectNameText
        projectDescText = binding.projectDescriptionText


        //set update button disabled as default
        setBtnDisabled()

        // Retrieve the project object from arguments
        val project = arguments?.getParcelable<Project>("project")

        // Access the properties of the Project object
        val cover = project?.cover
        val name = project?.name
        val startDate = project?.start_date
        val endDate = project?.end_date
        val fundTarget = project?.fund_target
        val fundCollected = project?.fund_collected
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


        //Navigation
        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_editProjectFragment_to_adminProjectFragment)
        }

        binding.EditProjectCancelButton.setOnClickListener{
            Toast.makeText(context, "Edit has been cancelled", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_editProjectFragment_to_adminProjectFragment)
        }

        binding.EditProjectUpdateButton.setOnClickListener{
            val editedProjectName = projectNameText.text.toString()
            val editedProjectDesc = projectDescText.text.toString()
            val edited = hashMapOf<String, Any>(
                "name" to editedProjectName,
                "description" to editedProjectDesc
            )

            editProject(project, edited)
            findNavController().navigate(R.id.action_editProjectFragment_to_adminProjectFragment)
        }

        return (binding.root)
    }

    private fun setBtnDisabled() {
        updateButton.isEnabled = false
        updateButton.setBackgroundColor(resources.getColor(R.color.button_disabled_bg))
    }

    private fun setBtnEnabled() {
        updateButton.isEnabled = true
        updateButton.setBackgroundColor(resources.getColor(R.color.accent_primary_300))
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
                        .addOnSuccessListener { Log.d("Edit Successful", "DocumentSnapshot successfully updated!") }
                        .addOnFailureListener { e -> Log.w("Edit Failed", "Error updating document", e) }
                } catch(e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "No project matched the query.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isFormInputValid(): Boolean {
        return (isNameValid && isDescValid)
    }


}
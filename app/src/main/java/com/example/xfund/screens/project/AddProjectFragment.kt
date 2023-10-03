package com.example.xfund.screens.project

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentAddProjectBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AddProjectFragment : Fragment() {
    private lateinit var binding: FragmentAddProjectBinding

    private lateinit var newCover: TextInputEditText
    private lateinit var newName: TextInputEditText
    private lateinit var newDescription: TextInputEditText
    private lateinit var newFundTarget: TextInputEditText

    private val db = Firebase.firestore
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseRef: DatabaseReference
    private var projectUri: Uri? = null

    // Define callBack function at the class level
    private lateinit var callBack: (String) -> Unit


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            projectUri = it
            binding.projectCover.setImageURI(it)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_add_project, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseRef = FirebaseDatabase.getInstance().getReference("projects/")
        storageRef = FirebaseStorage.getInstance().getReference("Images")


        //declare variable
        newCover = binding.projectNameText
        newName = binding.projectNameText
        newDescription = binding.projectDescriptionText
        newFundTarget = binding.projectFundTarget


        // Hide bottom nav when load this page
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }


        binding.backButton.setOnClickListener{
            findNavController().navigateUp()
        }

        // Edit Image
        binding.projectCover.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.addProjectCancelButton.setOnClickListener{
            Toast.makeText(requireContext(), "The process has been canceled", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }

        binding.addProjectAddButton.setOnClickListener{

            val hashName = newName.text.toString()
            val hashDesc = newDescription.text.toString()
            val hashTarget = newFundTarget.text.toString().toInt()

            uploadProjectImageToFirebaseStorage(projectUri, hashName)

            callBack = { imageUrl ->

                val newProject = hashMapOf(
                    "cover" to imageUrl,
                    "name" to hashName,
                    "description" to hashDesc,
                    "start_date" to FieldValue.serverTimestamp(),
                    "end_date" to FieldValue.serverTimestamp(),
                    "fund_target" to hashTarget,
                    "fund_collected" to 0
                )

                addNewProject(newProject)
                Toast.makeText(context, "Project has been added", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }

    }


    private fun uploadProjectImageToFirebaseStorage(imageUri: Uri?, coverName: String) {
        val imageRef = storageRef.child("projects/{$coverName}.jpg")
        if (imageUri != null) {
            imageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, get the download URL.
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        // Save the imageUrl to Firestore or Realtime Database.
                        callBack(imageUrl)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle upload failure.
                }
        }
    }

    // Add project
    private fun addNewProject(hashMap: HashMap<String, Any>) {
        db.collection("projects").document()
            .set(hashMap)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
/*
                Toast.makeText(context, "Project has been added", Toast.LENGTH_LONG).show()
*/
            }
            .addOnFailureListener {
                e -> Log.w("TAG", "Error writing document", e)
/*
                Toast.makeText(context, "Project added failed! Please try again.", Toast.LENGTH_LONG).show()
*/
            }
    }
}
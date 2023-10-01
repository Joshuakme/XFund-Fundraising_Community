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

/*
    var pickedPhoto: Uri? = null
    var pickedBitMap: Bitmap? = null
*/

    private lateinit var newCover: TextInputEditText
    private lateinit var newName: TextInputEditText
    private lateinit var newDescription: TextInputEditText
    private lateinit var newStartDate: TextInputEditText
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
        firebaseRef = FirebaseDatabase.getInstance().getReference("projects/")
        storageRef = FirebaseStorage.getInstance().getReference("Images")




        //declare variable
        newCover = binding.projectNameText
        newName = binding.projectNameText
        newDescription = binding.projectDescriptionText
        /*newStartDate = binding.projectStartDate*/
        newFundTarget = binding.projectFundTarget



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
                findNavController().navigate(R.id.action_addProjectFragment_to_adminProjectFragment)
            }
        }


        return binding.root
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
                Toast.makeText(requireContext(), "Project has been added", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                e -> Log.w("TAG", "Error writing document", e)
                Toast.makeText(requireContext(), "Project added failed! Please try again.", Toast.LENGTH_LONG).show()
            }
    }





    /*fun pickPhoto(view: View){
        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1)

        } else {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(galleryIntent, 2)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntext = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver,pickedPhoto!!)
                    pickedBitMap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(pickedBitMap)
                }
                else {
                    pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                    imageView.setImageBitmap(pickedBitMap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }*/


}
package com.example.xfund.screens.user

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.xfund.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfileFragment : Fragment() {
    private lateinit var binding: com.example.xfund.databinding.FragmentEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private val user = Firebase.auth.currentUser!!
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseRef: DatabaseReference
    private var uri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("user/")
        storageReference = FirebaseStorage.getInstance().getReference("Images")

        binding.btnSave.setOnClickListener {
            saveData()
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.EditImage.setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        binding.EditIcon.setOnClickListener {
            //Allow any format of images from any file manager to use
            pickImage.launch("image/*")
        }

        return binding.root


    }

    private fun saveData() {
        val username = binding.tfEditUsername.text.toString()
        val email = binding.tfEditEmail.text.toString()
        val password = binding.tfEditPassword.text.toString()

        if (username.isEmpty()) binding.tfEditUsername.error = "Please Write A Username"
        if (email.isEmpty()) binding.tfEditEmail.error = "Please Enter An Email"
        if (password.isEmpty()) binding.tfEditPassword.error = "Please Enter Password"

        val userProfileId = firebaseRef.push().key!!
        var userProfile: UserProfile

        uri?.let {
            storageReference.child(userProfileId).putFile(it)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            Toast.makeText(
                                context,
                                "Image Stored Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val imgUrl = url.toString()

                            userProfile = UserProfile(userProfileId, username, email, password, imgUrl)

                            firebaseRef.child(userProfileId).setValue(userProfile)
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        context,
                                        "Data Stored Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "error ${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                }
        }
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


    }

    public fun deleteAccount() {

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234")

//        // Prompt the user to re-provide their sign-in credentials
//        user.reauthenticate(credential)
//            .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
//
//            }
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }


    }
}
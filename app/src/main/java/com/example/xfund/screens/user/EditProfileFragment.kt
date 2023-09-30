package com.example.xfund.screens.user

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.xfund.R
import com.example.xfund.databinding.FragmentEditProfileBinding
import com.example.xfund.util.PickImageContract
import com.example.xfund.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text
import java.util.UUID

class EditProfileFragment : Fragment() {
    private lateinit var binding: com.example.xfund.databinding.FragmentEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseRef: DatabaseReference
    private var uri: Uri? = null
    private lateinit var sharedPreferences: SharedPreferences

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {

        if (it != null) {
            uri = it
            binding.EditImage.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variables
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("user/")
        storageRef = FirebaseStorage.getInstance().getReference("Images")
        sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)


        // ELEMENTS
        val usernameTxt = binding.tfEditUsername
        val emailTxt = binding.tfEditEmail
        val passwordTxt = binding.tfEditPassword

        // Check if logged in
        val isLogin: Boolean = sharedPreferences?.getBoolean("IsLogin", false) == true

        if(isLogin && FirebaseAuth.getInstance().currentUser != null) {
            user = FirebaseAuth.getInstance().currentUser!!
            if(user.displayName.isNullOrEmpty()) {
                usernameTxt.setText("Username")
            } else {
                usernameTxt.setText(user.displayName.toString())
            }

            emailTxt.setText(user.email.toString())
            passwordTxt.setText("")
        } else {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            return binding.root
        }

        // EVENT LISTENERS
        binding.btnSave.setOnClickListener {
            uri?.let { it1 -> updateUserDetail(binding.tfEditUsername.text.toString(), it1) }
        }

        // Delete Account
        binding.DeleteAccount.setOnClickListener{
           val message : String? = "Are you sure want to delete account?"
           showDeleteAccountDialog(message)
       }
        // Edit Image
        binding.EditIcon.setOnClickListener {
            pickImage.launch("image/*")
        }

        return binding.root
    }

    private fun uploadProfileImageToFirebaseStorage(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("user/${FirebaseAuth.getInstance().currentUser!!.uid}")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL.
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    // Save the imageUrl to Firestore or Realtime Database.

                    Toast.makeText(context, "Upload Successfully to Firestore!", Toast.LENGTH_SHORT)

                }
            }
            .addOnFailureListener { exception ->
                // Handle upload failure.
                }
    }


    private fun showDeleteAccountDialog(message: String?){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
        val btnYes : Button = dialog.findViewById(R.id.btnYes)
        val btnNo : Button = dialog.findViewById(R.id.btnNo)

        tvMessage.text = message

        btnYes.setOnClickListener{
            Toast.makeText(requireContext(), "Account Successfully Deleted", Toast.LENGTH_SHORT).show()


            val user = Firebase.auth.currentUser
            user?.delete()?.addOnCompleteListener{
                //Account Successfully Deleted
                if(it.isSuccessful){

                    // Remove login status from preference file / sharedPreference
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("IsLogin", false)
                    editor.commit()

                    // Message
                    Toast.makeText(requireContext(), "Account Successfully Deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_homeFragment)
                }else{
                    //catch error
                }
            }

            dialog.dismiss()
        }

        btnNo.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateUserDetail(displayName: String, imageUri: Uri) {
        // Upload photo to firebase
        //uploadProfileImageToFirebaseStorage(imageUri)


        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(imageUri)
            .setDisplayName(displayName)
            .build()

        // Update username, photoUrl to Firebase
        user.updateProfile(profileUpdates)
            .addOnCompleteListener {task ->
                Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT)

                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
            }
            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT)
            }

        // Update Email to Firebase
        if(binding.tfEditEmail.text.toString() != user.email) {
            user.updateEmail(binding.tfEditEmail.text.toString())
                .addOnCompleteListener {
                    // Email updated successfully
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT)
                }
        }

        // Update Password to Firebase
        if(!binding.tfEditPassword.text.isNullOrEmpty()) {
            user.updatePassword(binding.tfEditPassword.text.toString())
                .addOnCompleteListener {
                    // Email updated successfully
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT)
                }
        }

        val userViewModel = UserViewModel()

        userViewModel.setUser(getUserFromFirebase())
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


    }


    public fun getUserFromFirebase(): FirebaseUser {
        return FirebaseAuth.getInstance().currentUser!!
    }

//    public fun deleteAccount() {
//
//        // Get auth credentials from the user for re-authentication. The example below shows
//        // email and password credentials but there are multiple possible providers,
//        // such as GoogleAuthProvider or FacebookAuthProvider.
//        val credential = EmailAuthProvider
//            .getCredential("user@example.com", "password1234")
//
////        // Prompt the user to re-provide their sign-in credentials
////        user.reauthenticate(credential)
////            .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
////
////            }
//        user.delete()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "User account deleted.")
//                }
//            }
//
//
//    }
}
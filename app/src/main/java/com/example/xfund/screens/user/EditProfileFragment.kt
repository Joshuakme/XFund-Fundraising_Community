package com.example.xfund.screens.user

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.xfund.R
import com.example.xfund.databinding.FragmentEditProfileBinding
import com.example.xfund.util.FirebaseHelper
import com.example.xfund.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private var uri: Uri? = null
    private val firestoreRepository = FirebaseHelper()
    private var currentUserViewModel = UserViewModel()

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


        // ELEMENTS
        val profileImg = binding.EditImage
        val usernameTxt = binding.tfEditUsername
        val emailTxt = binding.tfEditEmail
        val passwordTxt = binding.tfEditPassword
        val auth = FirebaseAuth.getInstance()

        // Check if logged in
        currentUser = auth.currentUser!!

        if(currentUser != null) {
//            // Use a coroutine scope to get all discussions
//            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
//                val user = firestoreRepository.fetchUserDetails(currentUser.uid)
//
//                if(user != null) {
//                    Toast.makeText(context, user.imgUri.toString(), Toast.LENGTH_SHORT).show()
//                    profileImg.setImageURI(user.imgUri)
//                    usernameTxt.setText(user?.displayName ?: "Username")
//                    emailTxt.setText(user.email.toString())
//                    passwordTxt.setText("")
//                }
//            }

            currentUserViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    //profileImg.setImageURI(user.photoUrl)
                    usernameTxt.setText(user?.displayName ?: "Username")
                    emailTxt.setText(user.email.toString())
                    passwordTxt.setText("")

                    Glide.with(context)
                        .load(user.photoUrl)
                        .placeholder(R.drawable.baseline_account_circle)
                        .into(view?.findViewById(R.id.EditImage))
                }
            }
        } else {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            return binding.root
        }

        // EVENT LISTENERS
        binding.btnSave.setOnClickListener {
            updateUserDetail(binding.tfEditUsername.text.toString(), uri)
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

    private fun showDeleteAccountDialog(message: String?){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
        val btnDelete : Button = dialog.findViewById(R.id.btnYes)
        val btnCancel : Button = dialog.findViewById(R.id.btnNo)

        tvMessage.text = message

        btnDelete.setOnClickListener{

            val user = Firebase.auth.currentUser
            user?.delete()?.addOnCompleteListener{
                //Account Successfully Deleted
                if(it.isSuccessful) {
                    // Message
                    Toast.makeText(requireContext(), "Account Successfully Deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_homeFragment)
                }else{
                    //catch error
                }
            }

            dialog.dismiss()
        }

        btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateUserDetail(displayName: String, imageUri: Uri?) {
        lateinit var profileUpdates: UserProfileChangeRequest

        if(imageUri != null) {
            // Use a coroutine scope to upload the image to Firebase
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val imageUrl = firestoreRepository.uploadUserImage(imageUri)

                if (imageUrl != null) {
                    // Image uploaded successful to Firebase Storage & Ref in Firestore
                    profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(imageUrl))
                        .setDisplayName(displayName)
                        .build()

                    // Update username, imageUri to Firebase
                    currentUser.updateProfile(profileUpdates)
                        .addOnCompleteListener {task ->
                            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show()

                            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Handle the case where image upload failed (e.g., authentication issue or upload error)
                }
            }
        }else {
            profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            // Update username to Firebase
            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener {task ->
                    Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }

//        // Update username, photoUrl to Firebase
//        currentUser.updateProfile(profileUpdates)
//            .addOnCompleteListener {task ->
//                Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show()
//
//                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//            }

        // Update Email to Firebase
        if(binding.tfEditEmail.text.toString() != currentUser.email) {
            currentUser.updateEmail(binding.tfEditEmail.text.toString())
                .addOnCompleteListener {
                    // Email updated successfully
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }

        // Update Password to Firebase
        if(!binding.tfEditPassword.text.isNullOrEmpty()) {
            currentUser.updatePassword(binding.tfEditPassword.text.toString())
                .addOnCompleteListener {
                    // Email updated successfully
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }

        val currentUserViewModel = UserViewModel()

        currentUserViewModel.setUser(currentUser)
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
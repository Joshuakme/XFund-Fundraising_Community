package com.example.xfund.screens.community

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentDiscussionDetailBinding
import com.example.xfund.databinding.FragmentEditDiscussionBinding
import com.example.xfund.model.CommunityDiscussion
import com.example.xfund.util.FirebaseHelper
import com.example.xfund.util.LoginDialogFragment
import com.google.android.material.chip.Chip
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditDiscussionFragment : Fragment() {
    private lateinit var binding: FragmentEditDiscussionBinding
    private val firestoreRepository = FirebaseHelper()
    private var discussionTagsList: MutableList<String> = mutableListOf()
    private lateinit var updateBtn: Button
    // State Variables
    var isTitleValid: Boolean = false
    var isDescValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_discussion, container, false)

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Variable Declaration
        val discussionTitle = binding.titleEditText
        val discussionDesc = binding.descEditText
        val tagsEditText = binding.tagsEditText
        val deleteBtn = binding.deleteButton
        updateBtn = binding.updateButton


        // Set Values
        val discussionId = arguments?.getString("discussionId")
        discussionTitle.setText(arguments?.getString("title"))
        discussionDesc.setText(arguments?.getString("desc"))
        discussionTagsList = arguments?.getStringArray("tags")?.toMutableList() ?: mutableListOf()

        for (tag in discussionTagsList) {
            createTagsChip(tag)
        }

        // EVENT LISTENERS
        discussionTitle.addTextChangedListener {
            isTitleValid = it?.isNotEmpty() == true && it?.isNotBlank()!! && it.length >= 15
            isDescValid = discussionDesc.text?.isNotEmpty() == true && discussionDesc.text?.isNotBlank()!! && discussionDesc.length() >= 30

            if(isFormInputValid()) {
                setBtnEnabled()
            } else {
                setBtnDisabled()
            }
        }

        discussionDesc.addTextChangedListener {
            isTitleValid = discussionTitle.text?.isNotEmpty() == true && discussionTitle.text?.isNotBlank()!! && discussionTitle.length() >= 15
            isDescValid = it?.isNotEmpty() == true && it?.isNotBlank()!! && it.length >= 30

            if(isFormInputValid()) {
                setBtnEnabled()
            } else {
                setBtnDisabled()
            }
        }

        tagsEditText.setOnEditorActionListener { v, keyCode, event ->
            binding.tagsEditText.isEnabled = discussionTagsList.size != 4

            if(keyCode == EditorInfo.IME_ACTION_DONE && discussionTagsList.size>= 0 && discussionTagsList.size <= 5) {
                //if (keyCode == KeyEvent.KEYCODE_ENTER || event.action == KeyEvent.ACTION_UP) {
                binding.apply {
                    val tagName = tagsEditText.text.toString()
                    discussionTagsList.add(tagName)
                    createTagsChip(tagName)
                    tagsEditText.text?.clear()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        updateBtn.setOnClickListener {
            val updatedDiscussion = listOf(discussionTitle.text.toString(), discussionDesc.text.toString(), discussionTagsList.toList())

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val isUpdated = firestoreRepository.updateDiscussion(discussionId!!,updatedDiscussion as List<Any>)
                if (isUpdated == 0) {
                    // Handle the case where the discussion was successfully updated
                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_editDiscussionFragment_to_viewPostedCommunityFragment)
                } else if(isUpdated == 1) {
                    // Discussion not belong to this user
                    Toast.makeText(context, "You are not allowed to update this discussion", Toast.LENGTH_SHORT).show()
                }
                else if(isUpdated == 2) {
                    // User not authenticated
                    Toast.makeText(context, "Please Login to update this discussion", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        deleteBtn.setOnClickListener {
            // Prompt Dialog to confirm deletion
            showDeleteDiscussionDialog("Are you sure want to delete this discussion?")

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val isDeleted = firestoreRepository.deleteDiscussion(discussionId!!)

                if (isDeleted == 0) {
                    // Handle the case where the discussion was successfully updated
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_editDiscussionFragment_to_viewPostedCommunityFragment)
                } else if(isDeleted == 1) {
                    // Discussion not belong to this user
                    Toast.makeText(context, "You are not allowed to delete this discussion", Toast.LENGTH_SHORT).show()
                }
                else if(isDeleted == 2) {
                    // User not authenticated
                    Toast.makeText(context, "Please Login to delete this discussion", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setBtnDisabled() {
        updateBtn.isEnabled = false
        updateBtn.setBackgroundColor(resources.getColor(R.color.button_disabled_bg))
    }

    private fun setBtnEnabled() {
        updateBtn.isEnabled = true
        updateBtn.setBackgroundColor(resources.getColor(R.color.accent_primary_300))
    }

    private fun isFormInputValid(): Boolean {
        return (isTitleValid && isDescValid)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTagsChip(name: String) {
        val chip = Chip(context)

        chip.apply {
            text = name
            setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.md_theme_light_primary)))
            textSize = 12.0f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            chipStartPadding = 1.0f
            chipEndPadding = 1.0f
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.md_theme_light_inversePrimary))

            binding.apply {
                tagsChipEntryGroup.addView(chip as View)
                chip.setOnCloseIconClickListener {
                    discussionTagsList.remove(text)
                    tagsChipEntryGroup.removeView(chip as View)

                    if(discussionTagsList.size <= 5) {
                        binding.tagsEditText.isEnabled = true
                        binding.tagsEditText.isFocusable = true
                        binding.tagsEditText.requestFocus(4)
                    }
                }
            }
        }
    }

    private fun showDeleteDiscussionDialog(message: String?){
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
            val user = Firebase.auth.currentUser
            user?.delete()?.addOnCompleteListener{
                //Account Successfully Deleted
                if(it.isSuccessful){
                    // Message
                    Toast.makeText(requireContext(), "Account Successfully Deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_homeFragment)
                }else{
                    //catch error
                    Toast.makeText(requireContext(), "Account Not Deleted", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.dismiss()
        }

        btnNo.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }
}
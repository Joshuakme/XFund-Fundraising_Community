package com.example.xfund.screens.community

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentAddDiscussionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddDiscussionFragment : Fragment() {
    private lateinit var binding: FragmentAddDiscussionBinding
    private lateinit var backButton : ImageButton
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descEditText: TextInputEditText
    private lateinit var tagsEditText: TextInputEditText
    private val discussionTagsList: MutableList<String> = mutableListOf()
    lateinit var submitButton: MaterialButton
    private val db = Firebase.firestore     // Firestore

    // State Variables
    var isTitleValid: Boolean = false
    var isDescValid: Boolean = false



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // (Binding) Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_discussion,
            container,
            false
        )

        // Variables
        backButton = binding.backButton
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        titleEditText = binding.titleEditText
        descEditText = binding.descEditText
        tagsEditText = binding.tagsEditText
        submitButton = binding.submitButton


        // Hide bottom nav when load this page
        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }

        // Back button navigation
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_addDiscussionFragment_to_communityFragment)
        }

        // Set submit button default as inactive (disabled)
        setBtnDisabled()

        // EVENT LISTENERS
        titleEditText.addTextChangedListener {
            isTitleValid = titleEditText.text?.isNotEmpty() == true && titleEditText.text?.isNotBlank()!! && titleEditText.length() >= 15

            if(isFormInputValid()) {
                setBtnEnabled()
            } else {
                setBtnDisabled()
            }
        }

        descEditText.addTextChangedListener {
            isDescValid = descEditText.text?.isNotEmpty() == true && descEditText.text?.isNotBlank()!! && descEditText.length() >= 30

            if(isFormInputValid()) {
                setBtnEnabled()
            } else {
                setBtnDisabled()
            }
        }

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val desc = descEditText.text.toString()
            val tags = discussionTagsList

            it.requestFocus(4)

            // Send the data to firestore database
            writeNewDiscussion(title, desc, tags)
        }

        entryChip()

        return binding.root
    }

    private fun setBtnDisabled() {
        submitButton.isEnabled = false
        submitButton.setBackgroundColor(resources.getColor(R.color.button_disabled_bg))
    }

    private fun setBtnEnabled() {
        submitButton.isEnabled = true
        submitButton.setBackgroundColor(resources.getColor(R.color.accent_primary_300))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun entryChip() {
        binding.tagsEditText.setOnEditorActionListener { v, keyCode, event ->

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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTagsChip(name: String) {
        val chip = Chip(context)

        chip.apply {
            text = name
            chipIcon = ContextCompat.getDrawable(
                context,
                R.drawable.baseline_local_offer_24
            )
            isChipIconVisible = true
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
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

    private fun writeNewDiscussion(title: String, desc: String, tags: MutableList<String>) {
        val user = Firebase.auth.currentUser

        var author: String = user?.uid ?: ""

        val newDiscussion = hashMapOf(
            "title" to title,
            "desc" to desc,
            "tags" to tags,
            "author" to author,
            "createdOn" to FieldValue.serverTimestamp()
        )

        // Check if the input field is empty
        if(title.isBlank() || title.isEmpty()) return

        db.collection("discussions").document().set(newDiscussion)
            .addOnSuccessListener {
                // Write was successful!
                // Clear input field
                resetForm()
                Snackbar.make(this.binding.submitButton, "Discussion added successfully!", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addDiscussionFragment_to_communityFragment)
            }
            .addOnFailureListener {
                // Write failed
                Snackbar.make(this.binding.submitButton, "Discussion added failed! Please try again.", Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun resetForm() {
        val title = binding.titleEditText.text
        val desc = binding.descEditText.text
        val tags = binding.tagsEditText.text
        val tagsList = discussionTagsList

        // Remove all chips for tags
        binding.tagsChipEntryGroup.removeAllViews()

        // Clear all text in the input field
        title?.clear()
        desc?.clear()
        tags?.clear()
        tagsList.clear()
    }

    private fun isFormInputValid(): Boolean {
        return (isTitleValid && isDescValid)
    }
}
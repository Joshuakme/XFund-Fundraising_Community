package com.example.xfund.screens.community

import android.os.Build
import android.os.Bundle
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentAddDiscussionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar


class AddDiscussionFragment : Fragment() {
    private lateinit var binding: FragmentAddDiscussionBinding
    private lateinit var backButton : ImageButton
    private val discussionTagsList: MutableList<String> = mutableListOf()
    lateinit var submitButton: MaterialButton


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
        submitButton = binding.submitButton



        // Hide bottom nav when load this page
        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }


        // Event Listeners
        backButton.setOnClickListener {view: View ->
            findNavController().navigate(R.id.action_addDiscussionFragment_to_communityFragment)
        }

        entryChip()


        submitButton.setOnClickListener {
            // Submit the discussion form
            Snackbar.make(this.binding.submitButton, "Button clicked!", Snackbar.LENGTH_SHORT).show()
        }


        // Form Validation


        return binding.root
    }

    private fun setBtnDisabled() {
        submitButton.isEnabled = false
    }

    private fun setBtnEnabled() {
        submitButton.isEnabled = true
    }


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
}
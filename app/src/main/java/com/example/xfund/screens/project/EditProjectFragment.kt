package com.example.xfund.screens.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentEditProjectBinding


/**
 * A simple [Fragment] subclass.
 * Use the [EditProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProjectFragment : Fragment() {
    private lateinit var binding: FragmentEditProjectBinding

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

        binding.EditProjectCancelButton.setOnClickListener{
            findNavController().navigate(R.id.action_editProjectFragment_to_adminProjectFragment)
        }

        binding.EditProjectUpdateButton.setOnClickListener{
            findNavController().navigate(R.id.action_editProjectFragment_to_adminProjectFragment)
        }

        return (binding.root)
    }


}
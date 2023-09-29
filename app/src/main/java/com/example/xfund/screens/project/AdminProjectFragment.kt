package com.example.xfund.screens.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentAdminProjectBinding


/**
 * A simple [Fragment] subclass.
 * Use the [AdminProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminProjectFragment : Fragment() {
    private lateinit var binding : FragmentAdminProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_admin_project, container, false)

        binding.ProjectAddButton.setOnClickListener{
            findNavController().navigate(R.id.action_adminProjectFragment_to_editProjectFragment)
        }


        return binding.root
    }


}
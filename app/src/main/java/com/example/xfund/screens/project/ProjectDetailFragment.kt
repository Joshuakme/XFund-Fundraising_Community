package com.example.xfund.screens.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentProjectDetailBinding


class ProjectDetailFragment : Fragment() {
    private lateinit var binding : FragmentProjectDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_project_detail, container, false)

        binding.ProjectDetailBackButton.setOnClickListener{
            findNavController().navigate(R.id.action_projectDetailFragment_to_projectsFragment)
        }

        return binding.root
    }

}
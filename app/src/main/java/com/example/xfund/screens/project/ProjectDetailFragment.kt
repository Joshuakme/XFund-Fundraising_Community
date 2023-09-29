package com.example.xfund.screens.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import com.example.xfund.R
import com.example.xfund.databinding.FragmentProjectDetailBinding
import com.example.xfund.model.Project
import java.text.SimpleDateFormat


class ProjectDetailFragment : Fragment() {
    private lateinit var binding : FragmentProjectDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_project_detail, container, false)

        // Retrieve the project object from arguments
        val project = arguments?.getParcelable<Project>("project")

        // Now you can access the properties of the Project object
        val cover = project?.cover
        val name = project?.name
        val startDate = project?.start_date
        val endDate = project?.end_date
        val fundTarget = project?.fund_target
        val fundCollected = project?.fund_collected
        val description = project?.description

        // Variable Declaration
        val txtName = binding.projectDetailName
        val txtDesc = binding.ProjectDetailDescription
        val progressBarView = binding.ProjectDetailCardProgressBar


        // Set Values
        txtName.text = name
        txtDesc.text = description

        var progressBar = ((fundCollected ?: 0).toFloat() / (fundTarget ?: 1).toFloat() * 100).toInt()
        binding.projectDetailPercentageValue.text = progressBar.toString()


        // Set the calculated progress value to the ProgressBar
        progressBarView.progress = progressBar



        binding.ProjectDetailBackButton.setOnClickListener{
            findNavController().navigate(R.id.action_projectDetailFragment_to_projectsFragment)
        }

        binding.ProjectDetailDonateButton.setOnClickListener{
            findNavController().navigate(R.id.action_projectDetailFragment_to_adminProjectFragment)
        }

        return binding.root
    }
}


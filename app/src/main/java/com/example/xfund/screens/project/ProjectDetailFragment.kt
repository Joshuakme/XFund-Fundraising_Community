package com.example.xfund.screens.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.xfund.R
import com.example.xfund.databinding.FragmentProjectDetailBinding
import com.example.xfund.model.Project
import com.google.android.material.bottomnavigation.BottomNavigationView


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

        // Access the properties of the Project object
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
        val txtFundCollected = binding.projectDetailFundCollected
        val txtFundTarget = binding.projectFundTarget


        // Load image using Glide
        Glide.with(binding.projectDetailImage.context)
            .load(cover)
            .centerCrop()
            .into(binding.projectDetailImage)

        // Set Values into the xml
        txtName.text = name
        txtDesc.text = description
        //Make it into RM "fundCollected".00 form
        val formattedText = getString(R.string.MoneyPatternForProject, "", fundCollected)
        val formattedText2 = getString(R.string.MoneyPatternForProject, "", fundTarget)
        txtFundCollected.text = formattedText
        txtFundTarget.text = formattedText2



        //ProgressBar Calculation
        var progressBar = ((fundCollected ?: 0).toFloat() / (fundTarget ?: 1).toFloat() * 100).toInt()
        binding.projectDetailPercentageValue.text = progressBar.toString()
        progressBarView.progress = progressBar


        // Hide bottom nav when load this page
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }


        binding.ProjectDetailBackButton.setOnClickListener{
            findNavController().navigate(R.id.action_projectDetailFragment_to_projectsFragment)
        }

        binding.ProjectDetailDonateButton.setOnClickListener{
            findNavController().navigate(R.id.action_projectDetailFragment_to_adminProjectFragment)
        }

        return binding.root
    }
}


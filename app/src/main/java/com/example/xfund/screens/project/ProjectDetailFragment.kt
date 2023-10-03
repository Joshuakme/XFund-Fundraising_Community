package com.example.xfund.screens.project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the project object from arguments
        val project = arguments?.getParcelable<Project>("project")

        // Access the properties of the Project object
        val id = project?.id
        val cover = project?.cover
        val name = project?.name
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


        // Make the values into RM *k, RM *m
        var formattedTarget: Double? = fundTarget
        var formattedCollected: Double? = fundCollected
        var formattedText = ""
        var formattedText2 = ""

        formattedTarget?.let {
            if (it >= 1000000) {
                formattedTarget = it / 1000000
                formattedTarget = String.format("%.1f", formattedTarget).toDouble()
                formattedText2 = getString(R.string.Millions, formattedTarget.toString())
            } else if (it >= 10000) {
                formattedTarget = it / 1000
                formattedTarget = String.format("%.1f", formattedTarget).toDouble()
                formattedText2 = getString(R.string.Thousands, formattedTarget.toString())
            } else if (it < 10000) {
                formattedText2 = getString(R.string.MoneyPatternForProject, "", formattedTarget)
            }
        }
        formattedCollected?.let {
            if (it >= 1000000) {
                formattedCollected = it / 1000000
                formattedCollected = String.format("%.1f", formattedCollected).toDouble()
                formattedText = getString(R.string.Millions, formattedCollected.toString())
            } else if (it >= 10000) {
                formattedCollected = it / 1000
                formattedCollected = String.format("%.1f", formattedCollected).toDouble()
                formattedText = getString(R.string.Thousands, formattedCollected.toString())
            } else if (it < 10000) {
                formattedText = getString(R.string.MoneyPatternForProject, "", formattedCollected)
            }
        }

        txtFundCollected.text = formattedText
        txtFundTarget.text = formattedText2


        //ProgressBar Calculation
        val progressBar = ((fundCollected ?: 0).toFloat() / (fundTarget ?: 1).toFloat() * 100).toInt()
        binding.projectDetailPercentageValue.text = progressBar.toString()
        progressBarView.progress = progressBar


        // Hide bottom nav when load this page
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        if (bottomNav != null) {
            bottomNav.visibility = View.GONE
        }


        binding.ProjectDetailBackButton.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.ProjectDetailShareButton.setOnClickListener{
            val url = "https://www.youtube.com/watch?v=JZehdUU6VbQ"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, url)
            val chooser = Intent.createChooser(intent, "Share using....")
            startActivity(chooser)
        }

        binding.ProjectDetailDonateButton.setOnClickListener{
            val action = ProjectDetailFragmentDirections.actionProjectDetailFragmentToPayment(id.toString())

            findNavController().navigate(action)
        }

    }

}


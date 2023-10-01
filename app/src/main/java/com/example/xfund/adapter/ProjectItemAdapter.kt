package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xfund.databinding.ProjectsListCardCellBinding
import com.example.xfund.model.Project
import com.example.xfund.screens.navigation.ProjectsFragmentDirections

class ProjectItemAdapter(
    val context: Context,
    private val itemList: List<Project>,
    private val navController: NavController
) : RecyclerView.Adapter<ProjectItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProjectsListCardCellBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            // Navigate to FragmentB when item is clicked
            val action = ProjectsFragmentDirections
                .actionProjectsFragmentToProjectDetailFragment(
                    currentItem)
            navController.navigate(action)
        }
    }

    inner class ViewHolder(
        private val binding: ProjectsListCardCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {
            binding.project = project
            // Ensure data binding updates immediately
            binding.executePendingBindings()

            // Load image using Glide
            Glide.with(binding.cardImage.context)
                .load(project.cover)
                .into(binding.cardImage)
        }
    }

}
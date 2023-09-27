package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.R
import com.example.xfund.databinding.ProjectsListCardCellBinding
import com.example.xfund.model.Project

class ProjectItemAdapter(private val itemList: List<Project>) :
    RecyclerView.Adapter<ProjectItemAdapter.ViewHolder>() {

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
    }

    inner class ViewHolder(private val binding: ProjectsListCardCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {
            binding.project = project
            // Ensure data binding updates immediately
            binding.executePendingBindings()
        }
    }
}
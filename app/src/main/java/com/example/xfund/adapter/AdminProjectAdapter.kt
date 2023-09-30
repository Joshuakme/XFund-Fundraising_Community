package com.example.xfund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xfund.databinding.ProjectAdminListCardCellBinding
import com.example.xfund.model.Project

class AdminProjectAdapter(
    val context: Context,
    private val itemList: List<Project>,
    private val onEditButtonClickListener: OnEditButtonClickListener,
    private val onDeleteButtonClickListener: OnDeleteButtonClickListener
) : RecyclerView.Adapter<AdminProjectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProjectAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProjectAdminListCardCellBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface OnEditButtonClickListener {
        fun onEditClick(position: Int, project: Project)
    }

    interface OnDeleteButtonClickListener {
        fun onDeleteClick(position: Int, project: Project)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    inner class ViewHolder(
        private val binding: ProjectAdminListCardCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {
            binding.project = project
            // Ensure data binding updates immediately
            binding.executePendingBindings()

            // Load image using Glide
            Glide.with(binding.projectAdminCardImage.context)
                .load(project.cover)
                .into(binding.projectAdminCardImage)
        }

        init {
            binding.projectAdminCardEditButtonImage.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val project = itemList[position]
                    onEditButtonClickListener.onEditClick(position, project)
                }
            }

            binding.projectAdminCardDeleteButtonImage.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val project = itemList[position]
                    onDeleteButtonClickListener.onDeleteClick(position, project)
                }
            }
        }
    }
}
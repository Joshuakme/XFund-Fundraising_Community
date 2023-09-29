package com.example.xfund.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.databinding.ProjectsListCardCellBinding
import com.example.xfund.model.Project
import com.example.xfund.screens.navigation.ProjectsFragmentDirections

class ProjectItemAdapter(
    val context: Context, private val itemList: List<Project>, private val navController: NavController) :
    RecyclerView.Adapter<ProjectItemAdapter.ViewHolder>() {
    /*private var onClickListener: OnClickListener? = null*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProjectsListCardCellBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            /*if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem)
            }*/

            // Navigate to FragmentB when item is clicked
            val action = ProjectsFragmentDirections
                .actionProjectsFragmentToProjectDetailFragment(
                    currentItem)
            Log.d("ProjectItemAdapter", "Current item: $currentItem")
            navController.navigate(action)
        }
    }

    /*// A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Project)
    }*/

    inner class ViewHolder(
        private val binding: ProjectsListCardCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {
            binding.project = project
            // Ensure data binding updates immediately
            binding.executePendingBindings()
        }
    }
}
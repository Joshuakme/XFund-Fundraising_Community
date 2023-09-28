package com.example.xfund.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.databinding.ProjectsListCardCellBinding
import com.example.xfund.model.Project

class ProjectItemAdapter(private val itemList: List<Project>) :
    RecyclerView.Adapter<ProjectItemAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

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
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem)
            }
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Project)
    }

    inner class ViewHolder(
        private val binding: ProjectsListCardCellBinding/*,
        listener: onCardClickListener*/) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {
            binding.project = project
            // Ensure data binding updates immediately
            binding.executePendingBindings()
        }
    }
}
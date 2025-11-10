package com.example.smarttask.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttask.data.TaskEntity
import com.example.smarttask.databinding.ItemTaskBinding



class TaskAdapter(
    private val viewModel: TaskViewModel
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks = listOf<Task>()

    inner class TaskViewHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.taskTitle.text = task.title
        holder.binding.taskDueAt.text = "Due: ${Date(task.dueAt)}"
        holder.binding.checkComplete.isChecked = task.isCompleted

        holder.binding.checkComplete.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleStatus(task.id, isChecked)
        }
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}

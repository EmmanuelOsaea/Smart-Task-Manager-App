package com.example.smarttaskmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttaskmanager.data.TaskEntity
import com.example.smarttaskmanager.databinding.ItemTaskBinding
import com.example.smarttaskmanager.utils.DateUtils

class TaskAdapter(
    private val onToggle: (taskId: Int, isDone: Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks = listOf<TaskEntity>()

    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.taskTitle.text = task.title
        holder.binding.taskDueAt.text = task.dueAt?.let { "Due: ${DateUtils.formatDueDate(it)}" } ?: "No due date"
        holder.binding.checkComplete.isChecked = task.isCompleted

        holder.binding.checkComplete.setOnCheckedChangeListener { _, isChecked ->
            onToggle(task.id, isChecked)
        }
    }

    fun updateTasks(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}

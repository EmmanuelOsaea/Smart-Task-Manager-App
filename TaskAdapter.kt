package com.example.smarttask.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarttask.data.TaskEntity
import com.example.smarttask.databinding.ItemTaskBinding

class TaskAdapter(private var tasks: List<TaskEntity>, private val onClick: (TaskEntity)->Unit) :
    RecyclerView.Adapter<TaskAdapter.Tas.vh>() {

    inner class VH(val b: ItemTaskBinding): RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun getItemCount() = tasks.size
   
       override fun onBindViewHolder(holder: VH, position: Int) {
        val t = tasks[position]
        holder.b.tvTitle.text = t.title
        holder.b.tvDetails.text = t.details
        holder.b.tvPriority.text = t.priority
        holder.b.cbDone.isChecked = t.isCompleted

        holder.b.root.setOnClickListener { onClick(t) }
        holder.b.cbDone.setOnCheckedChangeListener { _, checked ->
            if (checked != t.isCompleted) onClick(t.copy(isCompleted = checked))
        }
    }

    override fun getItemCount() = tasks.size
    fun update(newList: List<TaskEntity>) { tasks = newList; notifyDataSetChanged() }
}

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

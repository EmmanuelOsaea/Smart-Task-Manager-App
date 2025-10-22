package com.example.taskmanager

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.data.Task
import com.example.taskmanager.ui.TaskViewModel
import com.example.taskmanager.ui.components.TaskAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter { task -> viewModel.delete(task) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        binding.fabAddTask.setOnClickListener { showAddTaskDialog() }
    }

    private fun showAddTaskDialog() {
        val inputTitle = EditText(this)
        inputTitle.hint = "Enter task title"
        MaterialAlertDialogBuilder(this)
            .setTitle("Add New Task")
            .setView(inputTitle)
            .setPositiveButton("Add") { _, _ ->
                val title = inputTitle.text.toString()
                if (title.isNotEmpty()) {
                    viewModel.insert(Task(title = title, description = ""))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

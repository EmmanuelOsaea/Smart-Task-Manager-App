package com.example.smarttaskmanager.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.smarttaskmanager.databinding.DialogAddEditTaskBinding
import com.example.smarttaskmanager.data.Task
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskDialog(
    context: Context,
    private val task: Task? = null,
    private val onSave: (Task) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogAddEditTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pre-fill fields if editing
        if (task != null) {
            binding.etTitle.setText(task.title)
            binding.etDescription.setText(task.description)
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val newTask = task?.copy(title = title, description = description, date = date)
                ?: Task(title = title, description = description, date = date)

            onSave(newTask)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}

package com.example.smarttask.ui

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import com.example.smarttask.data.TaskEntity
import com.example.smarttask.databinding.DialogAddEditTaskBinding

class AddEditTaskDialog(private val ctx: Context, private val onSave: (TaskEntity)->Unit) {
    fun show() {
        val b = DialogAddEditTaskBinding.inflate(LayoutInflater.from(ctx))
        val priorities = listOf("High","Medium","Low")
        b.spinnerPriority.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_item, priorities)

        AlertDialog.Builder(ctx)
            .setTitle("Add Task")
            .setView(b.root)
            .setPositiveButton("Save") { _, _ ->
                val t = TaskEntity(title = b.etTitle.text.toString(), details = b.etDetails.text.toString(), priority = b.spinnerPriority.selectedItem.toString())
                onSave(t)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

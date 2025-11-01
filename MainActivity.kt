package com.example.taskmanager
package com.example.smarttask.ui
package com.smarttaskmanager

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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import com.smarttaskmanager.utils.DateUtils
import java.util.*

val formattedDate = DateUtils.formatDueDate(System.currentTimeMillis())
binding.tvDueDate.text = formattedDate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

              tvDueDate = findViewById(R.id.tvDueDate)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)

        // Display current timestamp
        tvDueDate.text = "Due Date: ${DateUtils.formatDueDate(selectedTimestamp)}"

        btnPickDate.setOnClickListener {
            pickDateTime()
        }

        btnSaveTask.setOnClickListener {
            // Save or update task using selectedTimestamp
            val formatted = DateUtils.formatDueDate(selectedTimestamp)
            tvDueDate.text = "Saved Due Date: $formatted"
        }
    }

    private fun pickDateTime() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Then show time picker
                TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)

                        selectedTimestamp = calendar.timeInMillis
                        tvDueDate.text =
                            "Due Date: ${DateUtils.formatDueDate(selectedTimestamp)}"
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        }
       }
        adapter = TaskAdapter { task -> viewModel.delete(task) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        binding.fabAddTask.setOnClickListener { showAddTaskDialog() }
    }

      private fun handleTaskClick(t: TaskEntity) {
        // If checkbox toggled it will come here; update it
        vm.update(t)
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


private fun scheduleReminder(taskTitle: String, dueTimeMillis: Long) {
    val intent = Intent(this, ReminderReceiver::class.java)
    intent.putExtra("TASK_TITLE", taskTitle)

    val pendingIntent = PendingIntent.getBroadcast(
        this,
        taskTitle.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        dueTimeMillis,
        pendingIntent
    )
}

scheduleReminder(taskTitle, selectedDueDate.timeInMillis)


package com.example.smarttaskmanager.ui

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarttaskmanager.data.TaskEntity
import com.example.smarttaskmanager.databinding.ActivityMainBinding
import com.example.smarttaskmanager.receiver.ReminderReceiver
import com.example.smarttaskmanager.utils.DateUtils
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    private var selectedTimestamp: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        adapter = TaskAdapter { taskId, isDone ->
            viewModel.toggleStatus(taskId, isDone)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe tasks
        viewModel.allTasks.observe(this) { tasks ->
            adapter.updateTasks(tasks)
        }

        // Set initial due date
        binding.tvDueDate.text = "Due Date: ${DateUtils.formatDueDate(selectedTimestamp)}"

        // Pick date button
        binding.btnPickDate.setOnClickListener {
            pickDateTime()
        }

        // Save task button
        binding.btnSaveTask.setOnClickListener {
            showAddTaskDialog()
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

                TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        selectedTimestamp = calendar.timeInMillis
                        binding.tvDueDate.text =
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

    private fun showAddTaskDialog() {
        val dialog = AddEditTaskDialog(this) { task ->
            // Assign selected timestamp as due date
            val taskWithDue = task.copy(dueAt = selectedTimestamp)
            viewModel.insert(taskWithDue)

            // Schedule notification
            scheduleReminder(taskWithDue)
        }
        dialog.show()
    }

    private fun scheduleReminder(task: TaskEntity) {
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("TASK_TITLE", task.title)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        task.dueAt?.let { alarmManager.setExact(AlarmManager.RTC_WAKEUP, it, pendingIntent) }
    }
}

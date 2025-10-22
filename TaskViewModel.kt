package com.example.taskmanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.data.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    val allTasks by lazy { repository.allTasks }

    init {
        val dao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
    }

    fun insert(task: Task) = viewModelScope.launch { repository.insert(task) }
    fun delete(task: Task) = viewModelScope.launch { repository.delete(task) }
    fun update(task: Task) = viewModelScope.launch { repository.update(task) }
}

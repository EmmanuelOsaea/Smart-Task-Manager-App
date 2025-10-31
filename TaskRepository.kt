package com.example.smarttaskmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smarttaskmanager.model.Task

class TaskRepository(private val dao: TaskDao) {
    val allTasks = dao.getAllTasks()
    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun delete(task: Task) = dao.delete(task)
    suspend fun update(task: Task) = dao.update(task)
}

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

package com.smarttaskmanager.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.smarttaskmanager.model.Task
import com.smarttaskmanager.data.AppDatabase
import kotlinx.coroutines.flow.Flow
import androidx.room.Room

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

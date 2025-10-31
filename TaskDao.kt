package com.example.taskmanager.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.smarttaskmanager.model.Task

@Dao
interface TaskDao {
    
@Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)

    @Update
    suspend fun updateTask(task: Task)
}

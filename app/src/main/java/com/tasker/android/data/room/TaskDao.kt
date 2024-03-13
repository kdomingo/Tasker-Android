package com.tasker.android.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.tasker.android.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun save(task: Task) : Long

    @Query("SELECT * FROM Task")
    fun getTasks() : Flow<List<Task>>

    @Query("SELECT * FROM Task ORDER BY deadline ASC")
    fun getTasksAscending() : Flow<List<Task>>

    @Query("SELECT * FROM Task ORDER BY deadline DESC")
    fun getTasksDescending() : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE isCompleted = true")
    fun getTasksCompleted() : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE id = :taskId")
    fun getTaskById(taskId: Long): Flow<Task>

    @Delete
    fun deleteTask(task: Task)
}
package com.tasker.android.data.datasource

import com.tasker.android.data.Task
import com.tasker.android.data.room.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface TaskDatasource {
    suspend fun save(task: Task)
    suspend fun delete(task: Task)
    fun getTasks(ascending: Boolean = true): Flow<List<Task>>
    fun getCompleted(): Flow<List<Task>>
}

@Singleton
class TaskDatasourceImpl
@Inject constructor(private val taskDao: TaskDao) : TaskDatasource {

    override suspend fun save(task: Task) {
        taskDao.save(task)
    }

    override suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }

    override fun getTasks(ascending: Boolean): Flow<List<Task>> =
        if (ascending) taskDao.getTasksAscending() else taskDao.getTasksDescending()

    override fun getCompleted(): Flow<List<Task>> = taskDao.getTasksCompleted()
}
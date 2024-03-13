package com.tasker.android.data.repository

import com.tasker.android.data.Task
import com.tasker.android.data.datasource.TaskDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface TaskRepository {
    suspend fun save(task: Task)
    suspend fun delete(task: Task)
    fun getAll(ascending: Boolean = true): Flow<List<Task>>
    fun getCompleted(): Flow<List<Task>>
}

@Singleton
class TaskRepositoryImpl
@Inject constructor(private val datasource: TaskDatasource) : TaskRepository {

    override suspend fun save(task: Task) {
        datasource.save(task)
    }

    override suspend fun delete(task: Task) {
        datasource.delete(task)
    }

    override fun getAll(ascending: Boolean): Flow<List<Task>> = datasource.getTasks(ascending)

    override fun getCompleted(): Flow<List<Task>> = datasource.getCompleted()
}


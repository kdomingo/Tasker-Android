package com.tasker.android.data

import com.tasker.android.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface TaskService {
    suspend fun save(task: Task)
    suspend fun delete(task: Task)
    fun getAll(ascending: Boolean = true): Flow<List<Task>>
    fun getCompleted(): Flow<List<Task>>
}

@Singleton
class TaskServiceImpl
@Inject constructor(private val taskRepository: TaskRepository) : TaskService {

    override suspend fun save(task: Task) {
        taskRepository.save(task)
    }

    override suspend fun delete(task: Task) {
        taskRepository.delete(task)
    }

    override fun getAll(ascending: Boolean): Flow<List<Task>> = taskRepository.getAll(ascending)

    override fun getCompleted(): Flow<List<Task>> = taskRepository.getCompleted()
}
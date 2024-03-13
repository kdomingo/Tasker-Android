package com.tasker.android.main

import androidx.lifecycle.ViewModel
import com.tasker.android.data.TaskService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class CompletedTasksViewModel
@Inject constructor(
    private val tasksService: TaskService
) : ViewModel() {

    val tasks = tasksService.getAll().flowOn(Dispatchers.IO)
}
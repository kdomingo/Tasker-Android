package com.tasker.android.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasker.android.data.Task
import com.tasker.android.data.TaskService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TaskListViewModel
@Inject constructor(
    private val taskService: TaskService
) : ViewModel() {

    private val _saveTaskEvent = Channel<Unit>()
    val saveTaskEvent get() = _saveTaskEvent.receiveAsFlow()

    private val _showCompletedEvent = Channel<Unit>()
    val showCompletedEvent get() = _showCompletedEvent.receiveAsFlow()

    private var ascending = true
    private val _sortOrder = MutableStateFlow(ascending)

    val tasks = _sortOrder.asStateFlow().flatMapLatest(taskService::getAll)
        .shareIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed()
        )

    val completedTasks = taskService.getCompleted().flowOn(Dispatchers.IO)

    fun toggleSort() {

        viewModelScope.launch {
            ascending = ascending.not()
            _sortOrder.emit(ascending)
        }
    }

    fun save(task: Task) {
        viewModelScope.launch {
            taskService.save(task)
            _saveTaskEvent.send(Unit)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskService.delete(task)
        }
    }

    fun showCompleted() {
        viewModelScope.launch {
            _showCompletedEvent.send(Unit)
        }
    }
}
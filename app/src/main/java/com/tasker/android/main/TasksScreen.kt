package com.tasker.android.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasker.android.R
import com.tasker.android.data.Task
import com.tasker.android.ui.TaskCard
import com.tasker.android.ui.dialog.TaskDetailsDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TasksScreen(
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val completedTasks by viewModel.completedTasks.collectAsState(initial = emptyList())

    var showTaskDialog by remember {
        mutableStateOf(false)
    }

    var showCompletedScreen by remember {
        mutableStateOf(false)
    }

    var currentTask: Task? by remember {
        mutableStateOf(null)
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.saveTaskEvent.collectLatest {
                showTaskDialog = false
            }
        }

        scope.launch {
            viewModel.showCompletedEvent.collectLatest {
                showCompletedScreen = true
            }
        }
    }

    if (showTaskDialog) {
        TaskDetailsDialog(
            onDismiss = {
                showTaskDialog = false
                currentTask = null
            },
            onAuxButtonClicked = { task ->
                task?.let {
                    viewModel.save(it)
                }
            },
            onPositiveButtonClicked = { task ->
                task?.let {
                    viewModel.save(it)
                }
            },
            onNegativeButtonClicked = { task ->
                task?.let {
                    viewModel.delete(task)
                }
            },
            currentTask = currentTask
        )
    }

    AnimatedVisibility(
        visible = showCompletedScreen,
        enter = slideInHorizontally(),
        exit = fadeOut()
    ) {
        CompletedTasksScreen(onClose = {
            showCompletedScreen = false
        }, tasks = completedTasks)
    }

    TasksContent(tasks = tasks,
        onTaskItemClicked = { task ->
            currentTask = task
            showTaskDialog = true
        },
        onTaskCheckboxClicked = { task ->
            viewModel.save(task)
        },
        onActionButtonClicked = {
            showTaskDialog = true
        },
        onSortButtonClicked = {
            viewModel.toggleSort()
        },
        onViewCompletedClicked = {
            viewModel.showCompleted()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TasksContent(
    tasks: List<Task>,
    onTaskItemClicked: (Task) -> Unit = {},
    onTaskCheckboxClicked: (Task) -> Unit = {},
    onActionButtonClicked: () -> Unit = {},
    onSortButtonClicked: () -> Unit = {},
    onViewCompletedClicked: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.tasks),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                actions = {
                    IconButton(onClick = onViewCompletedClicked) {
                        Icon(Icons.Filled.Checklist, null)
                    }

                    IconButton(onClick = onSortButtonClicked) {
                        Icon(Icons.AutoMirrored.Filled.Sort, null)
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onActionButtonClicked,
                shape = CircleShape,
                containerColor = Color.Blue,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            if (tasks.isEmpty().not()) {
                items(tasks, { task -> task.id!! }) { task ->
                    TaskCard(
                        details = task,
                        onTaskCheckboxClicked = { currentTask ->
                            onTaskCheckboxClicked.invoke(
                                currentTask
                            )
                        },
                        onTaskClicked = { onTaskItemClicked.invoke(task) })
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskListPreview() {
    TasksContent(
        tasks = listOf(
            Task(
                1,
                "Feed the cats",
                "Feed the cats",
                null,
                false
            ),
            Task(
                2,
                "Feed the cats",
                "Feed the cats",
                null,
                true
            )
        )
    )
}
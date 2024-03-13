package com.tasker.android.ui.dialog

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.tasker.android.R
import com.tasker.android.data.Task
import com.tasker.android.ui.theme.AppDanger
import com.tasker.android.ui.theme.AppSecondary
import java.time.LocalDate

@Composable
fun TaskDetailsDialog(
    currentTask: Task? = null,
    onDismiss: () -> Unit,
    onAuxButtonClicked: (Task?) -> Unit,
    onPositiveButtonClicked: (Task?) -> Unit,
    onNegativeButtonClicked: (Task?) -> Unit
) {
    val defaultDialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.SecureOn
    )

    val inViewMode by remember {
        mutableStateOf(currentTask != null)
    }

    var inEditMode by remember {
        mutableStateOf(false)
    }

    val isCompleted = currentTask?.isCompleted ?: false

    val auxButtonLabel =
        if (null == currentTask) stringResource(R.string.save) else stringResource(R.string.complete_task)

    val positiveButtonLabel =
        if (inEditMode) stringResource(R.string.save) else stringResource(R.string.edit)

    var taskTitle by remember {
        mutableStateOf(currentTask?.title ?: "")
    }

    var taskDeadline by remember {
        mutableStateOf(currentTask?.deadline)
    }

    var taskDescription by remember {
        mutableStateOf(currentTask?.description ?: "")
    }

    var titleIsError by remember {
        mutableStateOf(false)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    if (showDatePicker) {
        TaskDetailsDatePicker(
            onDismiss = { showDatePicker = false },
            onConfirmButtonClicked = { dateInMillis ->
                taskDeadline = dateInMillis
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = defaultDialogProperties
    ) {
        Card {
            Column(Modifier.padding(16.dp)) {

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    IconButton(onClick = onDismiss, Modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = taskTitle,
                    placeholder = {
                        Text(stringResource(R.string.task_title))
                    }, onValueChange = { title ->
                        taskTitle = title
                        titleIsError = false
                    },
                    supportingText = if (titleIsError) (@Composable {
                        Text("Title required.")
                    }) else null,
                    isError = titleIsError,
                    readOnly = inViewMode and inEditMode.not(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = taskDeadline?.let { DateFormat.format("MMM dd, yyyy", it).toString() }
                        ?: "",
                    placeholder = {
                        Text(stringResource(R.string.deadline))
                    },
                    onValueChange = {
                    },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Filled.DateRange, null)
                        }
                    },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTrailingIconColor = Color.DarkGray,
                        disabledLabelColor = Color.DarkGray,
                        disabledPlaceholderColor = Color.DarkGray,
                        disabledTextColor = Color.DarkGray,
                        disabledBorderColor = Color.DarkGray,
                    ),
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        },
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = taskDescription,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 100.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(stringResource(R.string.task_description))
                    },
                    onValueChange = { description ->
                        taskDescription = description
                    },
                    readOnly = inViewMode and inEditMode.not(),
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (inEditMode.not()) {
                    ElevatedButton(
                        onClick = {
                            currentTask?.let { task ->
                                onAuxButtonClicked.invoke(task.copy(isCompleted = true))
                            } ?: run {
                                if (taskTitle.trim().isNotEmpty()) {
                                    onAuxButtonClicked.invoke(
                                        Task(
                                            title = taskTitle,
                                            deadline = taskDeadline,
                                            description = taskDescription
                                        )
                                    )
                                    onDismiss.invoke()
                                } else {
                                    titleIsError = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = AppSecondary,
                            contentColor = Color.White
                        ),
                        enabled = isCompleted.not(),
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(corner = CornerSize(8.dp))
                    ) {
                        Text(auxButtonLabel)
                    }
                }

                currentTask?.let { task ->
                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        ElevatedButton(
                            onClick = {
                                if (inEditMode.not()) {
                                    inEditMode = true
                                } else {
                                    if (taskTitle.trim().isEmpty()) {
                                        titleIsError = true
                                        return@ElevatedButton
                                    }
                                    onPositiveButtonClicked.invoke(
                                        task.copy(
                                            title = taskTitle,
                                            deadline = taskDeadline,
                                            description = taskDescription
                                        )
                                    )
                                }
                            },
                            enabled = isCompleted.not(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = AppSecondary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
                            shape = RoundedCornerShape(corner = CornerSize(8.dp))
                        ) {
                            Text(positiveButtonLabel)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        ElevatedButton(
                            onClick = {
                                onNegativeButtonClicked.invoke(currentTask)
                                onDismiss.invoke()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = AppDanger,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
                            shape = RoundedCornerShape(corner = CornerSize(8.dp))
                        ) {
                            Text(stringResource(R.string.delete))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskDetailsDialogPreview() {
    TaskDetailsDialog(
        onDismiss = {},
        onAuxButtonClicked = {},
        onNegativeButtonClicked = {},
        onPositiveButtonClicked = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDetailsDatePicker(
    onDismiss: () -> Unit,
    onConfirmButtonClicked: (Long?) -> Unit
) {

    val pickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= System.currentTimeMillis()
        }

        override fun isSelectableYear(year: Int): Boolean {
            return year >= LocalDate.now().year
        }
    })

    DatePickerDialog(onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirmButtonClicked.invoke(pickerState.selectedDateMillis)
                onDismiss.invoke()
            }) {
                Text(stringResource(R.string.ok).uppercase())
            }
        }, dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel).uppercase()) }
        }) {
        DatePicker(
            state = pickerState,
            showModeToggle = false,
            title = {
                Spacer(modifier = Modifier.height(16.dp))
            }
        )
    }
}
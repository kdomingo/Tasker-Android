@file:Suppress("UNUSED_EXPRESSION")

package com.tasker.android.ui

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tasker.android.data.Task

@Composable
fun TaskCard(
    details: Task,
    onTaskClicked: () -> Unit = {},
    onTaskCheckboxClicked: ((Task) -> Unit)? = null
) {
    val spacer10 = Spacer(modifier = Modifier.height(10.dp))
    val spacer16 = Spacer(modifier = Modifier.height(16.dp))
    val completed = details.isCompleted

    val detailsColor = if (completed) Color.DarkGray.copy(alpha = 0.5f) else Color.DarkGray
    val detailsDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None

    var taskDeadline = details.deadline?.let {
        DateFormat.format("MMM dd, yyyy", it).toString()
    } ?: ""

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(CornerSize(8.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { onTaskClicked.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    details.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = detailsColor,
                        textDecoration = detailsDecoration
                    )
                )
                spacer16
                Text(
                    taskDeadline,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = detailsColor,
                        textDecoration = detailsDecoration
                    )
                )
                spacer10
                Text(
                    details.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = detailsColor,
                        textDecoration = detailsDecoration
                    )
                )
            }
            Box(contentAlignment = Alignment.Center) {
                onTaskCheckboxClicked?.let { callback ->
                    Checkbox(
                        checked = completed,
                        onCheckedChange = {
                            callback.invoke(details.copy(isCompleted = it))
                        },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = Color.Blue.copy(alpha = 0.4f),
                            checkedColor = Color.Blue,
                            checkmarkColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskCardPreview() {
    TaskCard(
        details = Task(
            title = "Feed the cats",
            description = "Feeding felines",
            isCompleted = true
        )
    )
}
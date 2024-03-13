package com.tasker.android.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val title: String = "",
    val description: String = "",
    val deadline: Long? = null,
    val isCompleted: Boolean = false
)
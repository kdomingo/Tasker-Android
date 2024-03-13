package com.tasker.android.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tasker.android.data.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
package com.tasker.android.data.provider

import android.content.Context
import androidx.room.Room
import com.tasker.android.data.room.TaskDatabase
import javax.inject.Inject
import javax.inject.Singleton

interface DbProvider {
    fun getAppDatabase(): TaskDatabase
}

@Singleton
class DbProviderImpl
@Inject constructor(private val context: Context) : DbProvider {
    override fun getAppDatabase(): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, name = DB_NAME).build()

    companion object {
        const val DB_NAME = "task.db"
    }
}
package com.tasker.android.data

import android.content.Context
import com.tasker.android.data.datasource.TaskDatasource
import com.tasker.android.data.datasource.TaskDatasourceImpl
import com.tasker.android.data.provider.DbProvider
import com.tasker.android.data.provider.DbProviderImpl
import com.tasker.android.data.repository.TaskRepository
import com.tasker.android.data.repository.TaskRepositoryImpl
import com.tasker.android.data.room.TaskDao
import com.tasker.android.data.room.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDbProvider(context: Context): DbProvider = DbProviderImpl(context)

    @Provides
    @Singleton
    fun provideTaskDatabase(dbProvider: DbProvider): TaskDatabase = dbProvider.getAppDatabase()

    @Provides
    @Singleton
    fun providesTaskDao(taskDatabase: TaskDatabase): TaskDao = taskDatabase.taskDao()

    @Provides
    @Singleton
    fun provideTaskDatasource(taskDao: TaskDao): TaskDatasource =
        TaskDatasourceImpl(taskDao)

    @Provides
    @Singleton
    fun provideTaskRepository(datasource: TaskDatasource): TaskRepository =
        TaskRepositoryImpl(datasource)

    @Provides
    @Singleton
    fun provideTaskService(taskRepository: TaskRepository): TaskService =
        TaskServiceImpl(taskRepository)
}
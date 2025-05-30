package com.example.personaltasks.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class TaskRoomDb: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
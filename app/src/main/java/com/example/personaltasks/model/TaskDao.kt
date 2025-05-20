package com.example.personaltasks.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    fun createTask(task: Task): Long

    @Query("SELECT * FROM Task WHERE id = :id")
    fun retrieveContact(id: Int): Task

    @Query("SELECT * FROM Task")
    fun retrieveContacts(): MutableList<Task>

    @Update
    fun updateTask(task: Task): Int

    @Delete
    fun deleteTask(task: Task): Int
}
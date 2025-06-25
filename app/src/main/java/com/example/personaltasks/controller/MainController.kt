package com.example.personaltasks.controller

import androidx.room.Room
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskRoomDb
import com.example.personaltasks.ui.MainActivity

class MainController(mainActivity: MainActivity) {
    private val taskDao: TaskDao = Room.databaseBuilder(
        mainActivity, TaskRoomDb::class.java, "task-database"
    ).fallbackToDestructiveMigration(true).build().taskDao()

    fun insertTask(task: Task) {
        Thread{
            taskDao.createTask(task)
        }.start()
    }

    // Necessário chamar esses métodos fora da Thread principal para não travar a UI
    fun getTask(id: Int) = taskDao.retrieveTask(id)
    fun getAllTasks() = taskDao.retrieveTasks()

    fun modifyTask(task: Task) {
        Thread{
            taskDao.updateTask(task)
        }.start()
    }
    fun removeTask(task: Task) {
        Thread {
            taskDao.deleteTask(task)
        }.start()
    }
}
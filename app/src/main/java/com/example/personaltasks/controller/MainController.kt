package com.example.personaltasks.controller

import android.os.Message
import androidx.room.Room
import com.example.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskDatabaseFirebase
import com.example.personaltasks.model.TaskRoomDb
import com.example.personaltasks.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainController(private val mainActivity: MainActivity) {
    // Implementação com RoomDb
//    private val taskDao: TaskDao = Room.databaseBuilder(
//        mainActivity, TaskRoomDb::class.java, "task-database"
//    ).fallbackToDestructiveMigration(true).build().taskDao()
    private val taskDao: TaskDao = TaskDatabaseFirebase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)

    fun insertTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDao.createTask(task)
        }
    }

    fun getTask(id: Int) = taskDao.retrieveTask(id)

    fun getAllTasks() {
        databaseCoroutineScope.launch {
            val tasks = taskDao.retrieveTasks()
            mainActivity.getTasksHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_TASK_ARRAY, tasks.toTypedArray())
            })
        }
    }

    fun modifyTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDao.updateTask(task)
        }
    }

    fun removeTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDao.deleteTask(task)
        }
    }
}
package com.example.personaltasks.controller

import android.os.Message
import com.example.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskDatabaseFirebase
import com.example.personaltasks.ui.ActivityHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryController(private val historyActivity: ActivityHistory){
    private val taskDao: TaskDao = TaskDatabaseFirebase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)


    fun getTask(id: Int) = taskDao.retrieveTask(id)

    fun getAllRemovedTasks() {
        databaseCoroutineScope.launch {
            val tasks = taskDao.retrieveTasks().filter { it.removed }
            historyActivity.getTasksHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_TASK_ARRAY, tasks.toTypedArray())
            })
        }
    }

    fun restoreTask(task: Task) {
        databaseCoroutineScope.launch {
            task.removed = false
            taskDao.updateTask(task)
        }
    }
}
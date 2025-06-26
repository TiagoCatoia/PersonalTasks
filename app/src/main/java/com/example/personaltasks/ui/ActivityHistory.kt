package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.adapter.TaskHistoryAdapter
import com.example.personaltasks.controller.HistoryController
import com.example.personaltasks.controller.MainController
import com.example.personaltasks.databinding.ActivityHistoryBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASk
import com.example.personaltasks.model.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ActivityHistory: AppCompatActivity(), OnTaskHistoryClickListener {
    private val ahb: ActivityHistoryBinding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    private val tasks = mutableListOf<Task>()

    private val adapter: TaskHistoryAdapter by lazy {
        TaskHistoryAdapter(tasks, this)
    }

    private val historyController: HistoryController by lazy {
        HistoryController(this)
    }

    companion object {
        const val GET_TASKS_MESSAGE = 1
        const val GET_TASKS_INTERVAL = 2000L
    }

    val getTasksHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GET_TASKS_MESSAGE) {
                historyController.getAllRemovedTasks()
                sendMessageDelayed(
                    obtainMessage().apply {
                        what = GET_TASKS_MESSAGE
                    }, GET_TASKS_INTERVAL
                )
            } else {
                val taskArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY, Task::class.java)
                } else {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY)
                }
                tasks.clear()
                taskArray?.forEach {
                    tasks.addAll(listOf(it as Task))
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ahb.root)
        setSupportActionBar(ahb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "History"

        ahb.taskRv.adapter = adapter
        ahb.taskRv.layoutManager = LinearLayoutManager(this)

        getTasksHandler.sendMessageDelayed(
                Message().apply {
            what = GET_TASKS_MESSAGE
        }, GET_TASKS_INTERVAL
        )
    }

    override fun onRestoreTaskMenuItemClick(position: Int) {
        historyController.restoreTask(tasks[position])
        tasks.removeAt(position)
        adapter.notifyItemRemoved(position)
        Toast.makeText(this, "Task restored", Toast.LENGTH_SHORT).show()
    }

    override fun onDetailsTaskMenuItemClick(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, tasks[position])
            putExtra(EXTRA_VIEW_TASk, true)
            startActivity(this)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser == null) finish()
    }
}
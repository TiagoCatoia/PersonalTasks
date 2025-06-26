package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.controller.MainController
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASk
import com.example.personaltasks.model.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val tasks = mutableListOf<Task>()

    private val adapter: TaskAdapter by lazy {
        TaskAdapter(tasks, this)
    }

    private lateinit var tarl: ActivityResultLauncher<Intent>

    private val mainController: MainController by lazy {
        MainController(this)
    }

    companion object {
        const val GET_TASKS_MESSAGE = 1
        const val GET_TASKS_INTERVAL = 2000L
    }

    val getTasksHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GET_TASKS_MESSAGE) {
                mainController.getAllTasks()
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
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)

        supportActionBar?.subtitle = getString(R.string.task_list)
        // Substitui o icone padrão pelo personalizado_
        amb.toolbarIn.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_main_menu)

        amb.taskRv.adapter = adapter
        amb.taskRv.layoutManager = LinearLayoutManager(this)

        tarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                }
                else {
                    result.data?.getParcelableExtra<Task>(EXTRA_TASK)
                }
                task?.let { receivedTask ->
                    val position = tasks.indexOfFirst { it.id == receivedTask.id }
                    if (position == -1) {
                        tasks.add(receivedTask)
                        adapter.notifyItemInserted(tasks.lastIndex)
                        mainController.insertTask(receivedTask)
                    }
                    else {
                        tasks[position] = receivedTask
                        adapter.notifyItemChanged(position)
                        mainController.modifyTask(receivedTask)
                    }
                }
            }
        }

        getTasksHandler.sendMessageDelayed(
            Message().apply {
                what = GET_TASKS_MESSAGE
            }, GET_TASKS_INTERVAL
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.newTask_mi -> {
                tarl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            R.id.sign_out_mi -> {
                Firebase.auth.signOut()
                finish()
                true
            }
            else -> false
        }
    }

    override fun onEditTaskMenuItemClick(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, tasks[position])
            tarl.launch(this)
        }
    }

    override fun onRemoveTaskMenuItemClick(position: Int) {
        mainController.removeTask(tasks[position])
        tasks.removeAt(position)
        adapter.notifyItemRemoved(position)
        Toast.makeText(this, "Task removed", Toast.LENGTH_SHORT).show()
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
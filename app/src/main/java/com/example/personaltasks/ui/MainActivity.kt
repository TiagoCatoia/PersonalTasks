package com.example.personaltasks.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Task

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val tasks = mutableListOf<Task>()

    private val adapter: TaskAdapter by lazy {
        TaskAdapter(tasks)
    }

    private lateinit var tarl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)

        supportActionBar?.subtitle = getString(R.string.task_list)
        // Substitui o icone padrão pelo personalizado
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
                    }
                    else {
                        tasks[position] = receivedTask
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }
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
            else -> {
                false
            }
        }
    }
}
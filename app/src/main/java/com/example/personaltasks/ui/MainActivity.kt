package com.example.personaltasks.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Task

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val tasks = mutableListOf<Task>()

    private val adapter: TaskAdapter by lazy {
        TaskAdapter(tasks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)

        supportActionBar?.subtitle = getString(R.string.task_list)
        // Substitui o icone padrão pelo personalizado
        amb.toolbarIn.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_main_menu)

        amb.taskRv.adapter = adapter
        amb.taskRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.newTask_mi -> {
                val newTask = Task("Título", "Descrição", "Data")
                tasks.add(newTask)
                adapter.notifyItemInserted(tasks.lastIndex)
                true
            }
            else -> {
                false
            }
        }
    }
}
package com.example.personaltasks.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Task

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: ArrayAdapter<Task>
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)
        adapter = TaskAdapter(this, tasks)
        amb.taskListLv.adapter = adapter
        // Substitui o icone padrão pelo personalizado
        amb.toolbarIn.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_main_menu)
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
                adapter.notifyDataSetChanged()
                true
            }
            else -> {
                false
            }
        }
    }
}
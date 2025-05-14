package com.example.personaltasks.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Task

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: ArrayAdapter<String>
    private val tasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        adapter = ArrayAdapter(this, R.layout.item_task, tasks)
        amb.taskListLv.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.newTask_mi -> {
                val newTask = Task("Título", "Descrição", "Data")
                tasks.add(newTask.toString())
                adapter.notifyDataSetChanged()
                true
            }
            else -> {
                false
            }
        }
    }
}
package com.example.personaltasks.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.ActivityHistory

class TaskHistoryAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: ActivityHistory
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}
package com.example.personaltasks.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ItemTaskBinding
import com.example.personaltasks.model.Task

class TaskAdapter(context: Context, private val taskList: MutableList<Task>):
    ArrayAdapter<Task>(
        context,
        R.layout.item_task,
        taskList
    ){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = taskList[position]
        var taskView : View
        if (convertView == null) {
            // Se a View ainda não foi criada, inflar um novo layout e criar o ViewHolder
            ItemTaskBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            ).apply {
                // Armazena o ViewHolder como tag na root View para reutilização futura
                val taskViewHolder = TaskViewHolder(titleTv, descriptionTv, dateTv)
                taskView = root
                taskView.tag = taskViewHolder
            }
        }
        else {
            // Se a View já existe, então reutiliza
            taskView = convertView
        }

        // Atualiza os dados da View com as informações da tarefa atual
        val taskViewHolder = taskView.tag as TaskViewHolder
        taskViewHolder.titleTv.text = task.title
        taskViewHolder.descriptionTv.text = task.description
        taskViewHolder.dateTv.text = task.deadline

        return taskView
    }

    private data class TaskViewHolder(val titleTv: TextView, val descriptionTv: TextView, val dateTv: TextView)
}
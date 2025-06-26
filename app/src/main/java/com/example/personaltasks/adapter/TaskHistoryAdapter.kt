package com.example.personaltasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ItemTaskBinding
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.ActivityHistory

class TaskHistoryAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: ActivityHistory
): RecyclerView.Adapter<TaskHistoryAdapter.TaskViewHolder>() {

    // ViewHolder que representa visualmente cada item da lista de tarefas
    inner class TaskViewHolder(private val itb: ItemTaskBinding): RecyclerView.ViewHolder(itb.root) {
        // Associa os dados da tarefa às views do item e define o menu de contexto
        fun bind(task: Task) {
            itb.titleTv.text = task.title
            itb.descriptionTv.text = task.description
            itb.dateTv.text = task.deadline
            itb.finishedCb.setChecked(task.finished)

            // Clique longo exibe menu popup com opções (detalhes e restaurar)
            itb.root.setOnLongClickListener { view ->
                val menuPopUp = PopupMenu(view.context, view)
                menuPopUp.inflate(R.menu.history_context_menu)
                menuPopUp.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.details_mi -> {
                            onTaskClickListener.onDetailsTaskMenuItemClick(adapterPosition)
                            true
                        }
                        R.id.restore_mi -> {
                            onTaskClickListener.onRestoreTaskMenuItemClick(adapterPosition)
                            true
                        }
                        else -> false
                    }
                }
                menuPopUp.show()
                true
            }
        }
    }

    // Cria uma nova instância de ViewHolder quando necessário
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int = taskList.size
}
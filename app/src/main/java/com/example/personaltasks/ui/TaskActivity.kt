package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityTaskBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASk
import com.example.personaltasks.model.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskActivity: AppCompatActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)
        setSupportActionBar(atb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "New task"

        // Verifica se a Activity recebeu uma Task (modo edição ou visualização)
        val receivedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        }
        else {
            intent.getParcelableExtra<Task>(EXTRA_TASK)
        }
        receivedTask?.let {
            // Se recebeu, preenche os campos da interface com os dados da Task
            supportActionBar?.subtitle = "Edit task"
            with(atb) {
                titleEt.setText(it.title)
                descriptionEt.setText(it.description)
                finishedCb.setChecked(it.finished)

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val date = LocalDate.parse(it.deadline.trim(), formatter)
                dateDp.updateDate(date.year, date.monthValue, date.dayOfMonth)

                // Verifica se está em modo de visualização (detalhes)
                val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASk, false)
                // Se sim, desabilita a edição dos campos e oculta o botão de salvar
                if (viewTask) {
                    supportActionBar?.subtitle = "Details task"
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    dateDp.isEnabled = false
                    saveBt.visibility = View.GONE
                    finishedCb.isEnabled = false
                }
            }
        }

        with(atb) {
            saveBt.setOnClickListener {
                val selectedDate = LocalDate.of(dateDp.year, dateDp.month, dateDp.dayOfMonth)
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                Task(
                    receivedTask?.id?:hashCode(),
                    titleEt.text.toString(),
                    descriptionEt.text.toString(),
                    selectedDate.format(formatter),
                    finishedCb.isChecked
                ).let { task ->
                    Intent().apply {
                        putExtra(EXTRA_TASK, task)
                        setResult(RESULT_OK, this)
                    }
                }
                finish()
            }
        }

        atb.cancelBt.setOnClickListener {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser == null) finish()
    }
}
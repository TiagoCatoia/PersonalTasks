package com.example.personaltasks.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class TaskDatabaseFirebase: TaskDao {
    private val databaseReference = Firebase.database.getReference("tasks")
    private val tasks = mutableListOf<Task>()

    init {
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue<Task>()
                task?.let { newTask ->
                    if (!tasks.any { it.id == newTask.id })
                        tasks.add(newTask)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue<Task>()
                task?.let { editedTask ->
                    tasks[tasks.indexOfFirst { it.id == editedTask.id }] = editedTask
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task = snapshot.getValue<Task>()
                task?.let {
                    tasks.remove(it)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // NSA
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }

        })

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskMap = snapshot.getValue<Map<String, Task>>()
                tasks.clear()
                taskMap?.values?.also { tasks.addAll(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }

        })
    }

    override fun createTask(task: Task): Long {
        databaseReference.child(task.id.toString()).setValue(task)
        return 1L
    }

    override fun retrieveTask(id: Int): Task {
        return tasks[tasks.indexOfFirst { it.id == id }]
    }

    override fun retrieveTasks(): MutableList<Task> {
        return tasks
    }

    override fun updateTask(task: Task): Int {
        databaseReference.child(task.id.toString()).setValue(task)
        return 1
    }

    override fun deleteTask(task: Task): Int {
        databaseReference.child(task.id.toString()).removeValue()
        return 1
    }

}
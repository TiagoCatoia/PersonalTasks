package com.example.personaltasks.ui

sealed interface OnTaskClickListener {
    fun onEditTaskMenuItemClick(position: Int)
    fun onDeleteTaskMenuItemClick(position: Int)
    fun onDetailsTaskMenuItemClick(position: Int)
}
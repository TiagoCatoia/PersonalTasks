package com.example.personaltasks.ui

sealed interface OnTaskClickListener {
    fun onEditTaskMenuItemClick(position: Int)
    fun onRemoveTaskMenuItemClick(position: Int)
    fun onDetailsTaskMenuItemClick(position: Int)
}
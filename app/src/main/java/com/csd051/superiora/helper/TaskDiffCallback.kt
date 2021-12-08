package com.csd051.superiora.helper

import androidx.recyclerview.widget.DiffUtil
import com.csd051.superiora.data.entity.Task

class TaskDiffCallback (private val mOldTaskList: List<Task>, private val mNewTaskList: List<Task>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldTaskList.size
    }

    override fun getNewListSize(): Int {
        return mNewTaskList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldTaskList[oldItemPosition].id == mNewTaskList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldTaskList[oldItemPosition]
        val newEmployee = mNewTaskList[newItemPosition]
        return oldEmployee.title == newEmployee.title &&
                oldEmployee.id_parent == newEmployee.id_parent &&
                oldEmployee.id_firebase == newEmployee.id_firebase &&
                oldEmployee.dueDate == newEmployee.dueDate &&
                oldEmployee.triggerLink == newEmployee.triggerLink &&
                oldEmployee.details == newEmployee.details &&
                oldEmployee.isDone == newEmployee.isDone &&
                oldEmployee.isFavorite == newEmployee.isFavorite &&
                oldEmployee.isFavByParent == newEmployee.isFavByParent
    }


}
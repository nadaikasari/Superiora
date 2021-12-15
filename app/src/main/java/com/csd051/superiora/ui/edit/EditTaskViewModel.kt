package com.csd051.superiora.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class EditTaskViewModel (private val repository: SuperioraRepository) : ViewModel() {

    fun getChildTask(parentId: Int): LiveData<List<Task>> =
        repository.getChildTask(parentId)

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun insertChild(task: Task) {
        repository.insertTask(task)
    }

    fun deleteChild(task: Task) {
        repository.deleteTask(task)
    }

    fun deleteTask(id: Int) {
        repository.deleteAllTask(id)
    }

}
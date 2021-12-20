package com.csd051.superiora.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class DetailTaskViewModel (private val repository: SuperioraRepository) : ViewModel() {

    fun getChildTask(parentId: Int): LiveData<List<Task>> =
        repository.getChildTask(parentId)

    fun updateTask(task: Task?) {
        task?.let { repository.updateTask(it) }
    }
}
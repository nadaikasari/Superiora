package com.csd051.superiora.ui.add

import android.app.Application
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class AddTaskViewModel(private val repository: SuperioraRepository) : ViewModel() {

    fun insert(task: Task) {
        repository.insertTask(task)
    }


}
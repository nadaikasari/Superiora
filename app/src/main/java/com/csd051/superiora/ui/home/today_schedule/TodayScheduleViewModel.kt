package com.csd051.superiora.ui.home.today_schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class TodayScheduleViewModel(private val repository: SuperioraRepository) : ViewModel() {

    fun getTodayTask() : LiveData<List<Task>> = repository.getTodayTask()

    fun getChildTask(parentId: Int) : LiveData<List<Task>> = repository.getChildTask(parentId)

    fun getStaticChild(parentId: Int) : List<Task> = repository.getStaticChild(parentId)

    fun updateTask(task: Task) = repository.updateTask(task)

}
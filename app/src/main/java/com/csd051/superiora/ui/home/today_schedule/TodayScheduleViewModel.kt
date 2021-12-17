package com.csd051.superiora.ui.home.today_schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class TodayScheduleViewModel(private val repository: SuperioraRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getTodayTask(dateNow: String) : LiveData<List<Task>> = repository.getTodayTask(dateNow)

    fun getChildTask(parentId: Int) : LiveData<List<Task>> = repository.getChildTask(parentId)

    fun getStaticChild(parentId: Int) : List<Task> = repository.getStaticChild(parentId)

    fun updateTask(task: Task) = repository.updateTask(task)

}
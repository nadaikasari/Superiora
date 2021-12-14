package com.csd051.superiora.ui.home.yourtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class YourTaskViewModel(private val repository: SuperioraRepository) : ViewModel() {

    fun getAllTask() : LiveData<List<Task>> = repository.getAllTask()

    fun getRootTask(courseId: Int) : LiveData<List<Task>> = repository.getRootTask(courseId)

    fun getChildTask(parentId: Int) : LiveData<List<Task>> = repository.getChildTask(parentId)


}
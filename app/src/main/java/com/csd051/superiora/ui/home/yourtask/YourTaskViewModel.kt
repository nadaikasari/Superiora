package com.csd051.superiora.ui.home.yourtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.utils.TasksFilterType

class YourTaskViewModel(private val repository: SuperioraRepository) : ViewModel() {

    private val _filter = MutableLiveData<TasksFilterType>()

    val tasks: LiveData<PagedList<Task>> = _filter.switchMap {
        repository.getTaskbySort(it)
    }

    fun getRootTask(courseId: Int) : LiveData<List<Task>> = repository.getRootTask(courseId)

    fun getChildTask(parentId: Int) : LiveData<List<Task>> = repository.getChildTask(parentId)

    fun getStaticChild(parentId: Int) : List<Task> = repository.getStaticChild(parentId)

    fun updateTask(task: Task) = repository.updateTask(task)

    fun getActiveTask(courseId: Int) : LiveData<List<Task>> = repository.getactiveTask(courseId)



}
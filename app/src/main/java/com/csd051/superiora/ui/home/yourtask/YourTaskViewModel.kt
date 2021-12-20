package com.csd051.superiora.ui.home.yourtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class YourTaskViewModel(private val repository: SuperioraRepository) : ViewModel() {

    private val _filter = MutableLiveData<Int>()
    private val courseId : Int = 0
    var titleQuery: String = ""

    var tasks: LiveData<List<Task>> = _filter.switchMap {
        when (it) {
            0 -> repository.getRootTask(courseId)
            1 -> repository.getActiveTasks(courseId)
            2 -> repository.getCompletedTasks(courseId)
            3 -> repository.getFavoriteTasks(courseId)
            4 -> repository.getFilteredTask(courseId, titleQuery)
            else -> repository.getRootTask(courseId)
        }
    }

    fun setFilter(filter: Int) {
        _filter.value = filter
    }

    init {
        setFilter(0)
    }


    fun getChildTask(parentId: Int) : LiveData<List<Task>> = repository.getChildTask(parentId)

    fun getStaticChild(parentId: Int) : List<Task> = repository.getStaticChild(parentId)

    fun updateTask(task: Task) = repository.updateTask(task)

}
package com.csd051.superiora.ui.home.roadmaps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.vo.Resource

class RoadmapsViewModel (private val repository: SuperioraRepository) : ViewModel() {

    private val _filter = MutableLiveData<Int>()
    private var courseId : Int = 0
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

    fun setCourseId(courseId: Int) {
        this.courseId = courseId
    }

    init {
        setFilter(0)
    }

    fun getAllTask() : LiveData<List<Task>> = repository.getAllTask()

    fun getDataFromApi(currentSize: Int ,courseId: Int): LiveData<Resource<List<Task>>> = repository.getDataTask(currentSize, courseId)

    fun getChildTask(parentId: Int) : LiveData<List<Task>> = repository.getChildTask(parentId)

    fun getStaticChild(parentId: Int) : List<Task> = repository.getStaticChild(parentId)

    fun updateTask(task: Task) = repository.updateTask(task)

}
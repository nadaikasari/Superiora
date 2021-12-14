package com.csd051.superiora.ui.home.roadmaps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task

class RoadmapsViewModel (private val repository: SuperioraRepository) : ViewModel() {

    fun getDataFromApi(courseId: Int) =
        repository.getDataAPI(courseId)

    fun getRootTask(courseId: Int) : LiveData<List<Task>> = repository.getRootTask(courseId)

    fun getChildTask(parentId: Int) : LiveData<List<Task>> = repository.getChildTask(parentId)

}
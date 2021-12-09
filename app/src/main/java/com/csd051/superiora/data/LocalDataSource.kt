package com.csd051.superiora.data

import androidx.lifecycle.LiveData
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.room.SuperioraDao

class LocalDataSource private constructor(private val dao: SuperioraDao) {

    fun getAllTask(): LiveData<List<Task>> = dao.getAllTask()

    fun getRootTask(): LiveData<List<Task>> = dao.getRootTask()

    fun getChildById(parentId: Int): LiveData<List<Task>> = dao.getChildTask(parentId)

    fun insertTask(task: Task) {
        dao.insert(task)
    }

    fun delete(task: Task) {
        dao.delete(task)
    }

    fun update(task: Task) {
        dao.update(task)
    }

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(dao: SuperioraDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(dao).apply {
                INSTANCE = this
            }

    }
}
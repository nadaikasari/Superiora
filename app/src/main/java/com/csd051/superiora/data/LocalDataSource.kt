package com.csd051.superiora.data

import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.User
import com.csd051.superiora.data.room.SuperioraDao
import com.csd051.superiora.utils.FilterUtils
import com.csd051.superiora.utils.TasksFilterType

class LocalDataSource private constructor(private val dao: SuperioraDao) {

    fun getAllTask(): LiveData<List<Task>> = dao.getAllTask()

    fun getRootTask(courseId: Int): LiveData<List<Task>> = dao.getRootTask(courseId)

    fun getActivetask(courseId: Int): LiveData<List<Task>> = dao.getActiveTask(courseId)

    fun getChildById(parentId: Int): LiveData<List<Task>> = dao.getChildTask(parentId)

    fun getStaticChild(parentId: Int): List<Task> = dao.getStaticChildTask(parentId)

    fun getTodayTask(dateNow: String): LiveData<List<Task>> = dao.getTodayTask(dateNow)

    fun getUser(): LiveData<User> = dao.getUser()

    fun insertTask(task: Task) {
        dao.insert(task)
    }

    fun insertTask(task: List<Task>) {
        dao.insert(task)
    }

    fun delete(task: Task) {
        dao.delete(task)
    }

    fun update(task: Task) {
        dao.update(task)
    }

    fun insertNewUser(user: User) {
        dao.insertUser(user)
    }

    fun deleteUser(email: String) {
        dao.deleteUser(email)
    }

    fun updateDataUser(user: User) {
        dao.updateDataUser(user)
    }

    fun getTaskSort(filter: TasksFilterType) : LiveData<PagedList<Task>> {
        val query = FilterUtils.getFilteredQuery(filter)

        return dao.getTasks(query).toLiveData(Config(pageSize = 20))
    }

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(dao: SuperioraDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(dao).apply {
                INSTANCE = this
            }

    }
}
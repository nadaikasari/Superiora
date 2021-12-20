package com.csd051.superiora.data

import androidx.lifecycle.LiveData
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.User
import com.csd051.superiora.data.room.SuperioraDao
import com.csd051.superiora.utils.FilterUtils
import com.csd051.superiora.utils.TasksFilterType

class LocalDataSource private constructor(private val dao: SuperioraDao) {

    fun getAllTask(): LiveData<List<Task>> = dao.getAllTask()

    fun getRootTask(courseId: Int): LiveData<List<Task>> = dao.getRootTask(courseId)

    fun getChildById(parentId: Int): LiveData<List<Task>> = dao.getChildTask(parentId)

    fun getStaticChild(parentId: Int): List<Task> = dao.getStaticChildTask(parentId)

    fun getTodayTask(dateNow: String): LiveData<List<Task>> = dao.getTodayTask(dateNow)

    fun getNotificationTask(dateNow: String): List<Task> = dao.getNotificationTask(dateNow)

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

    fun getTaskSort(courseId:Int, filter: TasksFilterType) : LiveData<List<Task>> {
        val query = FilterUtils.getFilteredQuery(courseId, filter)
        return dao.getTasks(query)
    }

    fun getFilteredTask(courseId:Int, title: String) : LiveData<List<Task>> {
        return dao.getSearchTask(courseId, title)
    }

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(dao: SuperioraDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(dao).apply {
                INSTANCE = this
            }

    }
}
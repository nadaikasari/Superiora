package com.csd051.superiora.data

import androidx.lifecycle.LiveData
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.User
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.utils.TasksFilterType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SuperioraRepository(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val remoteDataSource: RemoteDataSource
) {

    private lateinit var firebase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    fun getAllTask(): LiveData<List<Task>> = localDataSource.getAllTask()

    fun getRootTask(courseId: Int): LiveData<List<Task>> = localDataSource.getRootTask(courseId)

    fun getChildTask(parentId: Int): LiveData<List<Task>> = localDataSource.getChildById(parentId)

    fun getStaticChild(parentId: Int): List<Task> = localDataSource.getStaticChild(parentId)

//    fun getSortTask(filter: TasksFilterType): LiveData<List<Task>> =
//        localDataSource.getTaskbySort(filter)

    fun getTodayTask(dateNow: String): LiveData<List<Task>> = localDataSource.getTodayTask(dateNow)

    fun insertTask(task: Task) {
        appExecutors.diskIO().execute {
            localDataSource.insertTask(task)
        }
    }

    fun insertAllTask(task: List<Task>) {
        appExecutors.diskIO().execute {
            localDataSource.insertTask(task)
        }
    }

    fun deleteTask(task: Task) {
        appExecutors.diskIO().execute {
            localDataSource.delete(task)
        }
    }

    fun updateTask(task: Task) {
        appExecutors.diskIO().execute {
            localDataSource.update(task)
        }
    }

    // ----------------------Ini Firebase------------------------
    fun register(users: User) {
        mAuth = FirebaseAuth.getInstance()
        firebase = FirebaseFirestore.getInstance()

        val user = mAuth.currentUser
        val df: DocumentReference = firebase.collection("users").document(user!!.uid)
        val userInfo: MutableMap<String, Any> = HashMap()
        userInfo["name"] = users.nama
        userInfo["photo"] = ""
        df.set(userInfo)
    }

    // ----------------------API Response------------------------
    fun getDataAPI(currentValue: Int, courseId: Int){
        remoteDataSource.getListData(currentValue, courseId, object : RemoteDataSource.LoadDataListCallback{
            override fun onAllDataReceived(dataListResponse: List<Task>) {
                insertAllTask(dataListResponse)
            }
        })
    }

    companion object {
        @Volatile
        private var instance: SuperioraRepository? = null
        fun getInstance(
            localData: LocalDataSource,
            appExecutors: AppExecutors,
            remoteData: RemoteDataSource
        ): SuperioraRepository =
            instance ?: synchronized(this) {
                SuperioraRepository(localData, appExecutors, remoteData).apply {
                    instance = this
                }
            }
    }
}
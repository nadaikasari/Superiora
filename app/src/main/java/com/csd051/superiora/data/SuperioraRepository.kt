package com.csd051.superiora.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.User
import com.csd051.superiora.data.room.SuperioraDao
import com.csd051.superiora.data.room.SuperioraDatabase
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SuperioraRepository(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) {

    private lateinit var firebase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    fun insertTask(task: Task) {
        appExecutors.diskIO().execute {
            localDataSource.insertTask(task)
        }
    }

    fun getAllTask(): LiveData<List<Task>> = localDataSource.getAllTask()


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

    companion object {
        @Volatile
        private var instance: SuperioraRepository? = null
        fun getInstance(
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): SuperioraRepository =
            instance ?: synchronized(this) {
                SuperioraRepository(localData, appExecutors).apply {
                    instance = this
                }
            }
    }
}
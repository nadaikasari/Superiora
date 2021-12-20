package com.csd051.superiora.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.User
import com.csd051.superiora.ui.home.home.HomeActivity
import com.csd051.superiora.ui.login.LoginActivity
import com.csd051.superiora.utils.AppExecutors
import com.csd051.superiora.utils.TasksFilterType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*


class SuperioraRepository(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val remoteDataSource: RemoteDataSource
) {

    private lateinit var firebase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val currentDate: String = dateFormat.format(Date())

    fun getAllTask(): LiveData<List<Task>> = localDataSource.getAllTask()

    fun getRootTask(courseId: Int): LiveData<List<Task>> = localDataSource.getRootTask(courseId)

    fun getChildTask(parentId: Int): LiveData<List<Task>> = localDataSource.getChildById(parentId)

    fun getStaticChild(parentId: Int): List<Task> = localDataSource.getStaticChild(parentId)

    fun getTodayTask(): LiveData<List<Task>> = localDataSource.getTodayTask(currentDate)

    fun getTodayNotification(): List<Task> = localDataSource.getNotificationTask(currentDate)

    fun getUser(): LiveData<User> = localDataSource.getUser()

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

    private fun insertNewUser(user: User) {
        appExecutors.diskIO().execute {
            localDataSource.insertNewUser(user)
        }
    }

    fun deleteUser(email: String) {
        appExecutors.diskIO().execute {
            localDataSource.deleteUser(email)
        }
    }

    private fun updateDataUser(user: User) {
        appExecutors.diskIO().execute {
            localDataSource.updateDataUser(user)
        }
    }

    fun getActiveTasks(courseId: Int): LiveData<List<Task>> {
        val filter = TasksFilterType.ACTIVE_TASKS
        return localDataSource.getTaskSort(courseId, filter)
    }

    fun getCompletedTasks(courseId: Int): LiveData<List<Task>> {
        val filter = TasksFilterType.COMPLETED_TASKS
        return localDataSource.getTaskSort(courseId, filter)
    }

    fun getFavoriteTasks(courseId: Int): LiveData<List<Task>> {
        val filter = TasksFilterType.FAVORITE_TASKS
        return localDataSource.getTaskSort(courseId, filter)
    }


    fun getFilteredTask(courseId: Int, title: String) : LiveData<List<Task>>{
        return localDataSource.getFilteredTask(courseId, title)
    }


    // ----------------------Firebase------------------------
    fun register(context: Context, user: User) {
        mAuth = FirebaseAuth.getInstance()
        firebase = FirebaseFirestore.getInstance()

        mAuth = FirebaseAuth.getInstance()
        firebase = FirebaseFirestore.getInstance()
        mAuth.createUserWithEmailAndPassword(user.email, user.password).addOnSuccessListener {
            val users = mAuth.currentUser
            val df: DocumentReference = firebase.collection("users").document(users!!.uid)
            val userInfo: MutableMap<String, Any> = HashMap()
            userInfo["name"] = user.nama
            userInfo["photo"] = ""
            df.set(userInfo)
            Toast.makeText(
                context,
                context.getString(R.string.register_success),
                Toast.LENGTH_SHORT
            )
                .show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(context, intent, null)
        }.addOnFailureListener {
            Toast.makeText(
                context,
                context.getString(R.string.register_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun login(context: Context, email: String, password: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            saveUser(authResult.user!!.uid, email, password)
            Toast.makeText(context, context.getString(R.string.login_success), Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(context, intent, null)
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveUser(uid: String, email: String, password: String) {
        firebase = FirebaseFirestore.getInstance()
        val df: DocumentReference = firebase.collection("users").document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            val name = documentSnapshot.getString("name").toString()
            val imageUrl = documentSnapshot.getString("photo").toString()
            val user = User(0, uid, name, email, password, imageUrl)
            insertNewUser(user)
        }
    }

    fun updateUser(context: Context, user: User, filePath: Uri) {
        storage = FirebaseStorage.getInstance()
        firebase = FirebaseFirestore.getInstance()
        storageReference = storage.reference

        val ref = storageReference.child("images/" + UUID.randomUUID().toString())
        ref.putFile(filePath).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { urlPhoto ->
                val data: MutableMap<String, Any> = HashMap()
                data["name"] = user.nama
                data["photo"] = urlPhoto.toString()

                firebase.collection("users").document(user.id_firebase).update(data)
                    .addOnSuccessListener {
                        val newUser = User(
                            user.id,
                            user.id_firebase,
                            user.nama,
                            user.email,
                            user.password,
                            urlPhoto.toString()
                        )
                        updateDataUser(newUser)
                        Toast.makeText(
                            context,
                            context.getString(R.string.edit_profile_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            context.getString(R.string.edit_profile_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    context.getString(R.string.edit_profile_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    fun updateDataUserWithNoUpdateImage(context: Context, user: User) {
        firebase = FirebaseFirestore.getInstance()
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = user.nama

        firebase.collection("users").document(user.id_firebase).update(data)
            .addOnSuccessListener {
                val newUser = User(
                    user.id,
                    user.id_firebase,
                    user.nama,
                    user.email,
                    user.password,
                    user.urlPhoto
                )
                updateDataUser(newUser)
                Toast.makeText(
                    context,
                    context.getString(R.string.edit_profile_success),
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    context.getString(R.string.edit_profile_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // ----------------------API Response------------------------
    fun getDataAPI(currentValue: Int, courseId: Int) {
        remoteDataSource.getListData(
            currentValue,
            courseId,
            object : RemoteDataSource.LoadDataListCallback {
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

        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true
    }
}
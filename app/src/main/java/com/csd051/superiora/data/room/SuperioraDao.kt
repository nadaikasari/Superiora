package com.csd051.superiora.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.User

@Dao
interface SuperioraDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: List<Task>)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAllTask(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id_parent = -1 AND id_course = :courseId ORDER BY id ASC")
    fun getRootTask(courseId: Int): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id_parent = :parentId ORDER BY id ASC")
    fun getChildTask(parentId: Int): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id_parent = :parentId ORDER BY id ASC")
    fun getStaticChildTask(parentId: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE dueDate = :dateNow AND isDone = 0")
    fun getTodayTask(dateNow: String): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE dueDate = :dateNow AND isDone = 0")
    fun getNotificationTask(dateNow: String): List<Task>

    @Query("SELECT * from tasks WHERE id_course = :courseId AND title LIKE '%' || :title || '%' ORDER BY id ASC")
    fun getSearchTask(courseId: Int, title: String): LiveData<List<Task>>

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * from user")
    fun getUser(): LiveData<User>

    @Query("DELETE FROM user WHERE email = :email")
    fun deleteUser(email: String)

    @Update
    fun updateDataUser(user: User)

    @RawQuery(observedEntities = [Task::class])
    fun getTasks(query: SupportSQLiteQuery): LiveData<List<Task>>

}
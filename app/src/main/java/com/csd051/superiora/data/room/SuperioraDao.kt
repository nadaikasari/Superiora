package com.csd051.superiora.data.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.csd051.superiora.data.entity.Task

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

    @Query("SELECT * from taskuser ORDER BY id ASC")
    fun getAllTask(): LiveData<List<Task>>

    @Query("SELECT * from taskuser WHERE id_parent = -1 AND id_course = :courseId ORDER BY id ASC")
    fun getRootTask(courseId: Int): LiveData<List<Task>>

    @Query("SELECT * from taskuser WHERE id_parent = :parentId ORDER BY id ASC")
    fun getChildTask(parentId: Int): LiveData<List<Task>>

    @Query("SELECT * from taskuser WHERE id_parent = :parentId ORDER BY id ASC")
    fun getStaticChildTask(parentId: Int): List<Task>

    @Query("DELETE FROM taskuser WHERE id = :id")
    fun deleteTask(id: Int)

    @RawQuery(observedEntities = [Task::class])
    fun getTasks(query: SupportSQLiteQuery): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM taskuser WHERE dueDate = :dateNow AND isDone = 0")
    fun getTodayTask(dateNow: String): LiveData<List<Task>>


}
package com.csd051.superiora.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.csd051.superiora.data.entity.Task

@Dao
interface SuperioraDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * from taskuser ORDER BY id ASC")
    fun getAllTask(): LiveData<List<Task>>
}
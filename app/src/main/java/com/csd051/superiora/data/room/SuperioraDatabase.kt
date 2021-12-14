package com.csd051.superiora.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.data.entity.User

@Database(entities = [User::class, Task::class], version = 2, exportSchema = false)
abstract class SuperioraDatabase : RoomDatabase() {
    abstract fun superioraDao() : SuperioraDao

    companion object {
        @Volatile
        private var INSTANCE: SuperioraDatabase? = null

        fun getInstance(context: Context): SuperioraDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SuperioraDatabase::class.java,
                    "superiora.db"
                ).fallbackToDestructiveMigration()
                    .build().apply {
                        INSTANCE = this
                    }
            }
    }

}
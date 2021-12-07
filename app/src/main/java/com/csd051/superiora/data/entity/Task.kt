package com.csd051.superiora.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskuser")
data class Task (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String
    )
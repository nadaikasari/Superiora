package com.csd051.superiora.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "taskuser")
@Parcelize
data class Task (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "id_course")
    var id_course: Int = 0,

    @ColumnInfo(name = "id_parent")
    var id_parent: Int = -1,

    @ColumnInfo(name = "id_firebase")
    var id_firebase: String? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "dueDate")
    var dueDate: String? = null,

    @ColumnInfo(name = "triggerLink")
    var triggerLink: String? = null,

    @ColumnInfo(name = "details")
    var details: String? = null,

    @ColumnInfo(name = "isDone")
    var isDone: Boolean = false,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,

    @ColumnInfo(name = "isRecomended")
    var isRecomended: Boolean = false,

    @ColumnInfo(name = "isDoneByParent")
    var isDoneByParent: Boolean = false
    ) : Parcelable
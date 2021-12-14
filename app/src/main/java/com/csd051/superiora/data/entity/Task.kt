package com.csd051.superiora.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "taskuser")
@Parcelize
data class Task (
    @field:SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "id_course")
    var id_course: Int = 0,

    @field:SerializedName("id_parent")
    @ColumnInfo(name = "id_parent")
    var id_parent: Int = -1,

    @ColumnInfo(name = "id_firebase")
    var id_firebase: String? = null,

    @field:SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "dueDate")
    var dueDate: String? = null,

    @field:SerializedName("triggerLink")
    @ColumnInfo(name = "triggerLink")
    var triggerLink: String? = null,

    @field:SerializedName("details")
    @ColumnInfo(name = "details")
    var details: String? = null,

    @ColumnInfo(name = "isDone")
    var isDone: Boolean = false,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,

    @field:SerializedName("isRecommend")
    @ColumnInfo(name = "isRecomended")
    var isRecomended: Boolean = false,

    @ColumnInfo(name = "isDoneByParent")
    var isDoneByParent: Boolean = false
    ) : Parcelable
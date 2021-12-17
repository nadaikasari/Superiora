package com.csd051.superiora.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "id_firebase")
    var id_firebase: String,

    @ColumnInfo(name = "nama")
    var nama : String,

    @ColumnInfo(name = "email")
    var email : String,

    @ColumnInfo(name = "password")
    var password : String,

    @ColumnInfo(name = "urlPhoto")
    var urlPhoto : String
) : Parcelable

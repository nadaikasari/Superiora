package com.csd051.superiora.data.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "nama")
    var nama : String,

    @ColumnInfo(name = "email")
    var email : String,

    @ColumnInfo(name = "password")
    var password : String,

    @ColumnInfo(name = "photo")
    var photo : String
) : Parcelable

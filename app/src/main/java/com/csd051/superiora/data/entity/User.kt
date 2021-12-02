package com.csd051.superiora.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var nama : String,
    var email : String,
    var password : String,
    var photo : String
) : Parcelable
